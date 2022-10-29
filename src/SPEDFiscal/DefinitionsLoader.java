package SPEDFiscal;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
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
    public static final String FIELD_DEF_VALIDATION = "validation";
    public static final String FIELD_DEF_REQUIRED = "required";

    String name;
    String pos;
    String type;
    String size;
    String dec;
    String format;
    String description;
    String ref;
    String validation;
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

class FieldFormat {
    private String format;
    private int maxSize;

    FieldFormat(String format, int maxSize){
        this.format = format;
        this.maxSize = maxSize;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
}

final class ValidationRegex {
    public static final String REGEX_DEF_NAME = "name";
    public static final String REGEX_DEF_EXPRESSION = "expression";
    public static final String REGEX_DEF_FAIL_MESSAGE = "fail_message";

    private final String name;
    private final String expression;
    private final String failMessage;
    private boolean required;

    ValidationRegex(String name, String expression, String failMessage) {
        this.name = name;
        this.expression = expression;
        this.failMessage = failMessage;
        this.required = true;
    }

    public String getName() {
        return name;
    }

    public String getExpression() {
        return expression;
    }

    public String getFailMessage() {
        return failMessage;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }
}

public class DefinitionsLoader {
    public static final String DEF_TAG_DEFINITIONS = "definitions";
    public static final String DEF_TAG_REGISTER = "register";
    public static final String DEF_TAG_FIELD = "field";
    public static final String DEF_TAG_FIELDS = "fields";
    public static final String DEF_TAG_REGEXS = "regexs";
    public static final String DEF_TAG_REGEX = "regex";
    public static final String FIELDS_REG_TYPE_STRING = "string";
    public static final String FIELDS_REG_TYPE_NUMBER = "number";
    public static final String FIELDS_REG_TYPE_DATE = "date";

    private static final HashMap<String, FieldFormat> fieldsFormat = new HashMap<>();
    private static final HashMap<String, ValidationRegex> validationsRegex = new HashMap<>();
    private static HashMap<String, FieldDefinitions[]> fieldsDefinitions = null;
    private static HashMap<String, RegisterDefinitions> registersDefinitions = null;

    protected static void addValidationRegex(ValidationRegex validationRegex) {
        validationsRegex.put(validationRegex.getName(), validationRegex);
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

    public static ValidationRegex getValidationRegex(String registerName, String fieldName) {
        String validationRegexName = "";
        for (FieldDefinitions fieldDefinitions : fieldsDefinitions.get(registerName)) {
            if (fieldDefinitions.name.equals(fieldName)) {
                validationRegexName = fieldDefinitions.validation;
                break;
            }
        }

        return validationsRegex.get(validationRegexName);
    }

    public static void addFieldFormat(String name, FieldFormat fieldFormat) {
        fieldsFormat.put(name, fieldFormat);
    }

    public static FieldFormat getFieldFormat(String name){
        return fieldsFormat.get(name);
    }

    public static void addFieldDefinitions(String name, FieldDefinitions[] fieldDefinitions) {
        if (fieldsDefinitions == null) fieldsDefinitions = new HashMap<>();
        fieldsDefinitions.put(name, fieldDefinitions);
    }

    public static void addRegisterDefinitions(String name, RegisterDefinitions registerDefinitions) {
        if (registersDefinitions == null) registersDefinitions = new HashMap<>();
        registersDefinitions.put(name, registerDefinitions);
    }

    public static FieldDefinitions[] getFieldsDefinitions(String name, String definitionsXmlPath) {
        if (fieldsDefinitions == null) {
            fieldsDefinitions = new HashMap<>();
            load(definitionsXmlPath);
        }

        return fieldsDefinitions.get(name);
    }

    public static RegisterDefinitions getRegisterDefinitions(String name, String definitionsXmlPath) {
        if (registersDefinitions == null) {
            registersDefinitions = new HashMap<>();
            load(definitionsXmlPath);
        }

        return registersDefinitions.get(name);
    }

    public static void load(String fieldsDefinitionsXmlPath) {
        try {
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            InputSource input = new InputSource(fieldsDefinitionsXmlPath);
            parser.parse(input, new DefinitionsHandler());
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }
}

class DefinitionsHandler extends DefaultHandler {
    private String registerName = "";
    private List<FieldDefinitions> fieldsDefinitions = null;

    public void startDocument() {}

    public void endDocument() {}

    public void startElement(String uri, String localName, String tag, Attributes attributes) {
        //start element register
        if (tag.equals(DefinitionsLoader.DEF_TAG_REGISTER)) {
            registerName = attributes.getValue(RegisterDefinitions.REGISTER_DEF_NAME);

            RegisterDefinitions registerDefinitions = new RegisterDefinitions();
            registerDefinitions.name = attributes.getValue(RegisterDefinitions.REGISTER_DEF_NAME);
            registerDefinitions.parent = attributes.getValue(RegisterDefinitions.REGISTER_DEF_PARENT);
            registerDefinitions.parentType = attributes.getValue(RegisterDefinitions.REGISTER_DEF_PARENT_TYPE);
            registerDefinitions.key = attributes.getValue(RegisterDefinitions.REGISTER_DEF_KEY);
            DefinitionsLoader.addRegisterDefinitions(registerName, registerDefinitions);
        }

        //start element fields
        if (tag.equals(DefinitionsLoader.DEF_TAG_FIELDS)) {
            fieldsDefinitions = new ArrayList<>();
        }

        //start element field
        if (!registerName.isEmpty() && tag.equals(DefinitionsLoader.DEF_TAG_FIELD)) {
            FieldDefinitions fieldDefinitions = new FieldDefinitions();

            fieldDefinitions.name = attributes.getValue(FieldDefinitions.FIELD_DEF_NAME);
            fieldDefinitions.pos = attributes.getValue(FieldDefinitions.FIELD_DEF_POS);
            fieldDefinitions.type = attributes.getValue(FieldDefinitions.FIELD_DEF_TYPE);
            fieldDefinitions.size = attributes.getValue(FieldDefinitions.FIELD_DEF_SIZE);
            fieldDefinitions.dec = attributes.getValue(FieldDefinitions.FIELD_DEF_DEC);
            fieldDefinitions.format = attributes.getValue(FieldDefinitions.FIELD_DEF_FORMAT);
            fieldDefinitions.description = attributes.getValue(FieldDefinitions.FIELD_DEF_DESCRIPTION);
            fieldDefinitions.ref = attributes.getValue(FieldDefinitions.FIELD_DEF_REF);
            fieldDefinitions.validation = attributes.getValue(FieldDefinitions.FIELD_DEF_VALIDATION);
            fieldDefinitions.required = attributes.getValue(FieldDefinitions.FIELD_DEF_REQUIRED);
            fieldsDefinitions.add(fieldDefinitions);

            FieldFormat fieldFormat = new FieldFormat(fieldDefinitions.format, Integer.parseInt("0" + fieldDefinitions.size));
            String fieldFormatName = registerName + "." + fieldDefinitions.name;

            DefinitionsLoader.addFieldFormat(fieldFormatName, fieldFormat);
        }

        //start element regex
        if (tag.equals(DefinitionsLoader.DEF_TAG_REGEX)) {
            DefinitionsLoader.addValidationRegex(
                    new ValidationRegex(
                            attributes.getValue(ValidationRegex.REGEX_DEF_NAME),
                            attributes.getValue(ValidationRegex.REGEX_DEF_EXPRESSION),
                            attributes.getValue(ValidationRegex.REGEX_DEF_FAIL_MESSAGE)
                    )
            );
        }
    }

    public void endElement(String uri, String localName, String tag) {
        //end element fields
        if (tag.equals(DefinitionsLoader.DEF_TAG_FIELDS)) {
            FieldDefinitions[] fd = new FieldDefinitions[fieldsDefinitions.size()];
            fieldsDefinitions.toArray(fd);
            DefinitionsLoader.addFieldDefinitions(registerName, fd);
        }
    }

    public void characters(char[] ch, int start, int length) {
    }
}
