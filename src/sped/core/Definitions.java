package sped.core;

import java.util.*;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.CharArrayWriter;
import java.io.IOException;

public class Definitions {

    public static class Builder {
        private final String xmlFile;
        private String fieldsSeparator = REGISTER_FIELD_SEPARATOR_DEFAULT;
        private String beginEndSeparator = REGISTER_FIELD_BEGIN_END_SEPARATOR_DEFAULT;
        private DefinitionsFileLoader definitionsFileLoader;
        private ValidationHelper validationHelper;

        public Builder(String xmlFile) {
            this.xmlFile = xmlFile;
        }

        public Builder setBeginEndSeparator(String registerBeginEndSeparator) {
            this.beginEndSeparator = registerBeginEndSeparator;
            return this;
        }

        public Builder setFieldsSeparator(String fieldsSeparator) {
            this.fieldsSeparator = fieldsSeparator;
            return this;
        }

        public Builder setFileLoader(DefinitionsFileLoader definitionsFileLoader) {
            this.definitionsFileLoader = definitionsFileLoader;
            return this;
        }

        public Builder setValidationHelper(ValidationHelper validationHelper) {
            this.validationHelper = validationHelper;
            return this;
        }

        public Definitions build(){
            var definitions = new Definitions(this.xmlFile);

            definitions.setBeginEndSeparator(this.beginEndSeparator);
            definitions.setFieldsSeparator(this.fieldsSeparator);
            definitions.setFileLoader(this.definitionsFileLoader);
            definitions.setValidationHelper(this.validationHelper);

            return definitions;
        }
    }

    public static final String REGISTER_FIELD_SEPARATOR_DEFAULT = "|";
    public static final String REGISTER_FIELD_BEGIN_END_SEPARATOR_DEFAULT = REGISTER_FIELD_SEPARATOR_DEFAULT;
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
    private final HashMap<String, FieldFormat> fieldsFormat = new HashMap<>();
    private final HashMap<String, Validation> validations = new HashMap<>();
    private volatile HashMap<String, FieldDefinitions[]> fieldsDefinitions = null;
    private volatile HashMap<String, RegisterDefinitions> registersDefinitions = null;
    private final String xmlFile;
    private DefinitionsFileLoader definitionsFileLoader;
    private ValidationHelper validationHelper;
    private String fieldsSeparator = REGISTER_FIELD_SEPARATOR_DEFAULT;
    private String beginEndSeparator = REGISTER_FIELD_BEGIN_END_SEPARATOR_DEFAULT;

    public Definitions(String xmlFile) {
        this.xmlFile = xmlFile;
    }
    public void setValidationHelper(ValidationHelper validationHelper) {
        this.validationHelper = validationHelper;
    }

    public void setBeginEndSeparator(String registerBeginEndSeparator) {
        this.beginEndSeparator = registerBeginEndSeparator;
    }

    public void setFieldsSeparator(String fieldsSeparator) {
        this.fieldsSeparator = fieldsSeparator;
    }

    public void setFileLoader(DefinitionsFileLoader definitionsFileLoader) {
        this.definitionsFileLoader = definitionsFileLoader;
    }

    public DefinitionsFileLoader getDefinitionsFileLoader() {
        return this.definitionsFileLoader;
    }

    public String getFieldsSeparator() {
        return fieldsSeparator;
    }

    public String getBeginEndSeparator() {
        return beginEndSeparator;
    }

    public ValidationHelper getValidationHelper() {
        return validationHelper;
    }

    protected void addValidation(Validation validation) {
        validations.put(validation.getName(), validation);
    }

    public Validation[] getValidations(String registerName, String fieldName) {

        String validationNames = Arrays.stream(fieldsDefinitions.get(registerName))
                .filter(fd -> fd.name.equals(fieldName))
                .map(d -> d.validationNames == null ? "" : d.validationNames)
                .findAny()
                .orElse(null);

        if (validationNames == null || validationNames.trim().equals(""))
            return null;

        String[] validationNamesArray = validationNames.split(",");
        Validation[] validationArray = new Validation[validationNamesArray.length];

        for (int i = 0; i < validationNamesArray.length; i++) {
            String validationName = validationNamesArray[i];
            Validation validation = validations.get(validationName.trim());

            if (validation == null) {
                validation = new ValidationReflection(getValidationHelper(), validationName.trim());
                addValidation(validation);
            }

            validationArray[i] = validation;
        }

        return validationArray;
    }

    public String getRequired(String registerName, String fieldName) {
        return Arrays.stream(fieldsDefinitions.get(registerName))
                .filter(fieldDefinitions -> fieldDefinitions.name.equals(fieldName))
                .map(definitions -> definitions.required)
                .findAny()
                .orElse("");
    }

    public void addFieldFormat(String name, FieldFormat fieldFormat) {
        fieldsFormat.put(name, fieldFormat);
    }

    public void addFieldDefinitions(String name, FieldDefinitions[] fieldDefinitions) {
        if (fieldsDefinitions == null) fieldsDefinitions = new HashMap<>();
        fieldsDefinitions.put(name, fieldDefinitions);
    }

    public void addRegisterDefinitions(String name, RegisterDefinitions registerDefinitions) {
        if (registersDefinitions == null) registersDefinitions = new HashMap<>();
        registersDefinitions.put(name, registerDefinitions);
    }

    public FieldDefinitions[] getFieldsDefinitions(String name) {
        if (fieldsDefinitions == null) {
            synchronized (Definitions.class) {
                if (fieldsDefinitions == null) {
                    fieldsDefinitions = new HashMap<>();
                    DefinitionsLoader.load(this);
                }
            }
        }

        return fieldsDefinitions.get(name);
    }

    public FieldFormat getFieldFormatByFieldName(String fieldName) {
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
                    DefinitionsLoader.load(this);
                }
            }
        }

        return registersDefinitions.get(registerName);
    }

    public Factory newFactory() {
        return new Factory(this);
    }
}

class DefinitionsLoader {
    public static void load(Definitions definitions) {
        try {
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            InputSource input = new InputSource(
                    definitions.getDefinitionsFileLoader().getInputStream(definitions.getXmlFile()));
            parser.parse(input, new DefinitionsHandler(definitions));

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }
}

class DefinitionsHandler extends DefaultHandler {
    private final CharArrayWriter validationScriptContents = new CharArrayWriter();
    private ValidationScript validationScript = null;

    private final Definitions definitions;
    private String registerName = "";
    private List<FieldDefinitions> fieldsDefinitions = null;

    DefinitionsHandler(Definitions definitions) {
        this.definitions = definitions;
    }

    public Definitions getDefinitions() {
        return this.definitions;
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
            definitions.addRegisterDefinitions(registerName, registerDefinitions);
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

            definitions.addFieldFormat(fieldFormatName, fieldFormat);
        }

        //start element regex
        if (tag.equals(Definitions.DEF_TAG_REGEX)) {
            definitions.addValidation(
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
            //File xmlFile = scriptFileName != null ? new File(this.getDefinitions().getXmlFile()) : null;

            validationScript = new ValidationScript(
                    attributes.getValue(ValidationScript.SCRIPT_DEF_NAME),
                    scriptFileName,
                    this.definitions.getDefinitionsFileLoader()
            );

            definitions.addValidation(validationScript);
        }
    }

    public void endElement(String uri, String localName, String tag) {
        //end element fields
        if (tag.equals(Definitions.DEF_TAG_FIELDS)) {
            FieldDefinitions[] fd = new FieldDefinitions[fieldsDefinitions.size()];
            fieldsDefinitions.toArray(fd);
            definitions.addFieldDefinitions(registerName, fd);
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

class FieldDefinitions {
    public static final String FIELD_EMPTY_STRING = "";
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