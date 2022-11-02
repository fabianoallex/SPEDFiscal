package SPEDFiscal;

import java.util.Date;

public class FieldsCreator {
    private final String definitionsXmlFile;

    FieldsCreator(String definitionsXmlFile) {
        this.definitionsXmlFile = definitionsXmlFile;
    }

    Fields create(String registerName) {
        Fields fields = new Fields();

        for (FieldDefinitions fieldDefinitions : DefinitionsLoader.getFieldsDefinitions(registerName, definitionsXmlFile)) {
            String fieldName = fieldDefinitions.name;
            String type = fieldDefinitions.type;
            String size = fieldDefinitions.size;
            String dec = fieldDefinitions.dec;
            String format = fieldDefinitions.format;
            String ref = fieldDefinitions.ref;

            if (type.equals(DefinitionsLoader.FIELDS_REG_TYPE_STRING)) fields.addField(new Field<String>(fieldName));
            if (type.equals(DefinitionsLoader.FIELDS_REG_TYPE_DATE)) fields.addField(new Field<Date>(fieldName));

            if (type.equals(DefinitionsLoader.FIELDS_REG_TYPE_NUMBER))
                fields.addField(dec.isEmpty() ?
                        new Field<Integer>(fieldName) :
                        new Field<Double>(fieldName));
        }

        return fields;
    }
}
