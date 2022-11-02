package SPEDFiscal;

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

public class DefinitionsLoader {
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
