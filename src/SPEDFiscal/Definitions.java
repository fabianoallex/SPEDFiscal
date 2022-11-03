package SPEDFiscal;

import java.util.HashMap;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.CharArrayWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class FieldDefinitions {
    public static final String FIELD_EMPTY_STRING = "";
    public static final String FIELD_SEPARATOR = "|";
    public static final String FIELD_DEF_NAME = "name";
    public static final String FIELD_DEF_POS = "pos";
    public static final String FIELD_DEF_TYPE = "type";
    public static final String FIELD_DEF_SIZE = "size";
    public static final String FIELD_DEF_DEC = "dec";
    public static final String FIELD_DEF_FORMAT = "format";
    public static final String FIELD_DEF_DESCRIPTION = "description";
    public static final String FIELD_DEF_REF = "ref";
    public static final String FIELD_DEF_VALIDATIONS = "validations";
    public static final String FIELD_DEF_REQUIRED = "required";

    String name;
    String pos;
    String type;
    String size;
    String dec;
    String format;
    String description;
    String ref;
    String validationNames;
    String required;
}

class RegisterDefinitions {
    public static final String REGISTER_DEF_NAME = "name";
    public static final String REGISTER_DEF_PARENT_TYPE = "parent_type";
    public static final String REGISTER_DEF_PARENT = "parent";
    public static final String REGISTER_DEF_KEY = "key";

    String name;
    String parentType;
    String parent;
    String key;
}

public class Definitions {
    public static final String DEF_TAG_DEFINITIONS = "definitions";
    public static final String DEF_TAG_REGISTER = "register";
    public static final String DEF_TAG_FIELD = "field";
    public static final String DEF_TAG_FIELDS = "fields";
    public static final String DEF_TAG_REGEXS = "regexs";
    public static final String DEF_TAG_REGEX = "regex";
    public static final String DEF_TAG_SCRIPT = "script";
    public static final String FIELDS_REG_TYPE_STRING = "string";
    public static final String FIELDS_REG_TYPE_NUMBER = "number";
    public static final String FIELDS_REG_TYPE_DATE = "date";
    private static final HashMap<String, FieldFormat> fieldsFormat = new HashMap<>();
    private static final HashMap<String, Validation> validations = new HashMap<>();
    private static volatile HashMap<String, FieldDefinitions[]> fieldsDefinitions = null;
    private static volatile HashMap<String, RegisterDefinitions> registersDefinitions = null;
    private final String xmlFile;

    public Definitions(String xmlFile) {
        this.xmlFile = xmlFile;
    }

    protected static void addValidation(Validation validation) {
        validations.put(validation.getName(), validation);
    }

    public static Validation[] getValidations(String registerName, String fieldName) {
        String validationNames = "";
        for (FieldDefinitions fieldDefinitions : fieldsDefinitions.get(registerName)) {
            if (fieldDefinitions.name.equals(fieldName)) {
                validationNames = fieldDefinitions.validationNames;
                break;
            }
        }

        if (validationNames == null || validationNames.trim().equals(""))
            return null;

        String[] validationNamesArray = validationNames.split(",");
        Validation[] validationArray = new Validation[validationNamesArray.length];

        for (int i = 0; i < validationNamesArray.length; i++) {
            validationArray[i] = validations.get(validationNamesArray[i].trim());
        }

        return validationArray;
    }

    public static String getRequired(String registerName, String fieldName) {
        String result = "";

        for (FieldDefinitions fieldDefinitions : fieldsDefinitions.get(registerName)) {
            if (fieldDefinitions.name.equals(fieldName)) {
                result = fieldDefinitions.required;
                break;
            }
        }

        return result;
    }

    public static void addFieldFormat(String name, FieldFormat fieldFormat) {
        fieldsFormat.put(name, fieldFormat);
    }

    public static void addFieldDefinitions(String name, FieldDefinitions[] fieldDefinitions) {
        if (fieldsDefinitions == null) fieldsDefinitions = new HashMap<>();
        fieldsDefinitions.put(name, fieldDefinitions);
    }

    public static void addRegisterDefinitions(String name, RegisterDefinitions registerDefinitions) {
        if (registersDefinitions == null) registersDefinitions = new HashMap<>();
        registersDefinitions.put(name, registerDefinitions);
    }

    public static FieldDefinitions[] getFieldsDefinitions(String name, String definitionsXmlFile) {
        if (fieldsDefinitions == null) {
            synchronized (Definitions.class) {
                if (fieldsDefinitions == null) {
                    fieldsDefinitions = new HashMap<>();
                    DefinitionsLoader.load(definitionsXmlFile);
                }
            }
        }

        return fieldsDefinitions.get(name);
    }

    public static FieldFormat getFieldFormatByFieldName(String fieldName) {
        return fieldsFormat.get(fieldName);
    }

    public String getXmlFile() {
        return xmlFile;
    }

    RegisterDefinitions getRegisterDefinitions(String registerName) {
        if (registersDefinitions == null) {
            synchronized(this) {
                if (registersDefinitions == null) {
                    registersDefinitions = new HashMap<>();
                    DefinitionsLoader.load(this.getXmlFile());
                }
            }
        }

        return registersDefinitions.get(registerName);
    }
}

class DefinitionsLoader {
    public static void load(String xmlFile) {
        try {
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            InputSource input = new InputSource(xmlFile);
            parser.parse(input, new DefinitionsHandler(xmlFile));
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }
}

class DefinitionsHandler extends DefaultHandler {
    private final CharArrayWriter validationScriptContents = new CharArrayWriter();
    private ValidationScript validationScript = null;

    private final String xmlFile;
    private String registerName = "";
    private List<FieldDefinitions> fieldsDefinitions = null;

    DefinitionsHandler(String xmlFile) {
        this.xmlFile = xmlFile;
    }

    public String getXmlFile() {
        return this.xmlFile;
    }

    public void startDocument() {}

    public void endDocument() {}

    public void startElement(String uri, String localName, String tag, Attributes attributes) {
        validationScriptContents.reset();

        //start element register
        if (tag.equals(Definitions.DEF_TAG_REGISTER)) {
            registerName = attributes.getValue(RegisterDefinitions.REGISTER_DEF_NAME);

            RegisterDefinitions registerDefinitions = new RegisterDefinitions();
            registerDefinitions.name = attributes.getValue(RegisterDefinitions.REGISTER_DEF_NAME);
            registerDefinitions.parent = attributes.getValue(RegisterDefinitions.REGISTER_DEF_PARENT);
            registerDefinitions.parentType = attributes.getValue(RegisterDefinitions.REGISTER_DEF_PARENT_TYPE);
            registerDefinitions.key = attributes.getValue(RegisterDefinitions.REGISTER_DEF_KEY);
            Definitions.addRegisterDefinitions(registerName, registerDefinitions);
        }

        //start element fields
        if (tag.equals(Definitions.DEF_TAG_FIELDS)) {
            fieldsDefinitions = new ArrayList<>();
        }

        //start element field
        if (!registerName.isEmpty() && tag.equals(Definitions.DEF_TAG_FIELD)) {
            FieldDefinitions fieldDefinitions = new FieldDefinitions();

            fieldDefinitions.name = attributes.getValue(FieldDefinitions.FIELD_DEF_NAME);
            fieldDefinitions.pos = attributes.getValue(FieldDefinitions.FIELD_DEF_POS);
            fieldDefinitions.type = attributes.getValue(FieldDefinitions.FIELD_DEF_TYPE);
            fieldDefinitions.size = attributes.getValue(FieldDefinitions.FIELD_DEF_SIZE);
            fieldDefinitions.dec = attributes.getValue(FieldDefinitions.FIELD_DEF_DEC);
            fieldDefinitions.format = attributes.getValue(FieldDefinitions.FIELD_DEF_FORMAT);
            fieldDefinitions.description = attributes.getValue(FieldDefinitions.FIELD_DEF_DESCRIPTION);
            fieldDefinitions.ref = attributes.getValue(FieldDefinitions.FIELD_DEF_REF);
            fieldDefinitions.validationNames = attributes.getValue(FieldDefinitions.FIELD_DEF_VALIDATIONS);
            fieldDefinitions.required = attributes.getValue(FieldDefinitions.FIELD_DEF_REQUIRED);
            fieldsDefinitions.add(fieldDefinitions);

            FieldFormat fieldFormat = new FieldFormat(fieldDefinitions.format, Integer.parseInt("0" + fieldDefinitions.size));
            String fieldFormatName = registerName + "." + fieldDefinitions.name;

            Definitions.addFieldFormat(fieldFormatName, fieldFormat);
        }

        //start element regex
        if (tag.equals(Definitions.DEF_TAG_REGEX)) {
            Definitions.addValidation(
                    new ValidationRegex(
                            attributes.getValue(ValidationRegex.REGEX_DEF_NAME),
                            attributes.getValue(ValidationRegex.REGEX_DEF_EXPRESSION),
                            attributes.getValue(ValidationRegex.REGEX_DEF_FAIL_MESSAGE)
                    )
            );
        }

        //start element script
        if (tag.equals(Definitions.DEF_TAG_SCRIPT)) {
            validationScriptContents.reset();

            String scriptFileName = attributes.getValue(ValidationScript.SCRIPT_DEF_FILE);
            File xmlFile = scriptFileName != null ? new File(this.getXmlFile()) : null;

            validationScript = new ValidationScript(
                    attributes.getValue(ValidationScript.SCRIPT_DEF_NAME),
                    scriptFileName != null ?  xmlFile.getParent() + File.separator + scriptFileName : ""
            );

            Definitions.addValidation(validationScript);
        }
    }

    public void endElement(String uri, String localName, String tag) {
        //end element fields
        if (tag.equals(Definitions.DEF_TAG_FIELDS)) {
            FieldDefinitions[] fd = new FieldDefinitions[fieldsDefinitions.size()];
            fieldsDefinitions.toArray(fd);
            Definitions.addFieldDefinitions(registerName, fd);
        }

        //end element script
        if (tag.equals(Definitions.DEF_TAG_SCRIPT)) {
            validationScript.setScript(validationScriptContents.toString());
        }
    }

    public void characters(char[] ch, int start, int length) {
        validationScriptContents.write( ch, start, length );
    }
}
