package SPEDFiscal;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
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

    String name;
    String pos;
    String type;
    String size;
    String dec;
    String format;
    String description;
    String ref;
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

public class DefinitionsLoader {
    private static HashMap<String, FieldDefinitions[]> fieldsDefinitions = null;
    private static HashMap<String, RegisterDefinitions[]> registersDefinitions = null;
    public static final String FIELDS_REG_TYPE_STRING = "string";
    public static final String FIELDS_REG_TYPE_NUMBER = "number";
    public static final String FIELDS_REG_TYPE_DATE = "date";

    public static void addFieldDefinitions(String name, FieldDefinitions[] fieldDefinitions) {
        if (fieldsDefinitions == null) {
            fieldsDefinitions = new HashMap<>();
        }

        fieldsDefinitions.put(name, fieldDefinitions);
    }

    public static void addRegisterDefinitions(String name, RegisterDefinitions[] registerDefinitions) {
        if (registersDefinitions == null) {
            registersDefinitions = new HashMap<>();
        }

        registersDefinitions.put(name, registerDefinitions);
    }

    public static FieldDefinitions[] getFieldsRegDefinitions(String name, String definitionsXmlPath) {
        if (fieldsDefinitions == null) {
            fieldsDefinitions = new HashMap<>();
            load(definitionsXmlPath);
        }

        return fieldsDefinitions.get(name);
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
    private List<RegisterDefinitions> registersDefinitions = null;

    public void startDocument() {
        registersDefinitions = new ArrayList<>();
    }

    public void endDocument() {
        RegisterDefinitions[] rd = new RegisterDefinitions[registersDefinitions.size()];
        registersDefinitions.toArray(rd);
        DefinitionsLoader.addRegisterDefinitions(registerName, rd);
    }

    public void startElement(String uri, String localName, String tag, Attributes attributes) {
        if (tag.equals("register")) {
            registerName = attributes.getValue(RegisterDefinitions.REGISTER_DEF_NAME);
            fieldsDefinitions = new ArrayList<>();

            RegisterDefinitions registerDefinitions = new RegisterDefinitions();
            registerDefinitions.name = attributes.getValue(RegisterDefinitions.REGISTER_DEF_NAME);
            registerDefinitions.parent = attributes.getValue(RegisterDefinitions.REGISTER_DEF_PARENT);
            registerDefinitions.parentType = attributes.getValue(RegisterDefinitions.REGISTER_DEF_PARENT_TYPE);
            registerDefinitions.key = attributes.getValue(RegisterDefinitions.REGISTER_DEF_KEY);

            registersDefinitions.add(registerDefinitions);
        }

        if (!registerName.isEmpty() && tag.equals("field")) {
            FieldDefinitions fieldDefinitions = new FieldDefinitions();

            fieldDefinitions.name = attributes.getValue(FieldDefinitions.FIELD_DEF_NAME);
            fieldDefinitions.pos = attributes.getValue(FieldDefinitions.FIELD_DEF_POS);
            fieldDefinitions.type = attributes.getValue(FieldDefinitions.FIELD_DEF_TYPE);
            fieldDefinitions.size = attributes.getValue(FieldDefinitions.FIELD_DEF_SIZE);
            fieldDefinitions.dec = attributes.getValue(FieldDefinitions.FIELD_DEF_DEC);
            fieldDefinitions.format = attributes.getValue(FieldDefinitions.FIELD_DEF_FORMAT);
            fieldDefinitions.description = attributes.getValue(FieldDefinitions.FIELD_DEF_DESCRIPTION);
            fieldDefinitions.ref = attributes.getValue(FieldDefinitions.FIELD_DEF_REF);

            fieldsDefinitions.add(fieldDefinitions);
        }
    }

    public void endElement(String uri, String localName, String tag) {
        if (tag.equals("register")) {
            FieldDefinitions[] fd = new FieldDefinitions[fieldsDefinitions.size()];
            fieldsDefinitions.toArray(fd);
            DefinitionsLoader.addFieldDefinitions(registerName, fd);
        }
    }

    public void characters(char[] ch, int start, int length) {
    }
}
