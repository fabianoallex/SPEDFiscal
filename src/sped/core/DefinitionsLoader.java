package sped.core;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DefinitionsLoader {
    public static void load(Definitions definitions) {
        try {
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            InputSource input = new InputSource(
                    definitions
                    .getDefinitionsFileLoader()
                    .getInputStream(definitions.getXmlFile())
            );

            parser.parse(input, new DefinitionsHandler(definitions));

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }
}

class DefinitionsHandler extends DefaultHandler {
    private final CharArrayWriter validationScriptContents = new CharArrayWriter();
    private ScriptValidation scriptValidation = null;

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
                    new RegexValidation(
                            attributes.getValue(RegexValidation.REGEX_DEF_NAME),
                            attributes.getValue(RegexValidation.REGEX_DEF_EXPRESSION),
                            attributes.getValue(RegexValidation.REGEX_DEF_FAIL_MESSAGE)
                    )
            );
        }

        //start element script
        if (tag.equals(Definitions.DEF_TAG_SCRIPT)) {
            validationScriptContents.reset();

            String scriptFileName = attributes.getValue(ScriptValidation.SCRIPT_DEF_FILE);

            scriptValidation = new ScriptValidation(
                    attributes.getValue(ScriptValidation.SCRIPT_DEF_NAME),
                    scriptFileName,
                    this.definitions.getDefinitionsFileLoader()
            );

            definitions.addValidation(scriptValidation);
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
            scriptValidation.setScript(validationScriptContents.toString());
        }
    }

    public void characters(char[] ch, int start, int length) {
        validationScriptContents.write( ch, start, length );
    }
}