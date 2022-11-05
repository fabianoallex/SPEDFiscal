package SPEDFiscalLib;

import java.util.Date;

public class FieldsCreator {
    static public Fields create(String registerName, Definitions definitions) {
        Fields fields = new Fields();

        for (FieldDefinitions fieldDefinitions : definitions.getFieldsDefinitions(registerName)) {
            String fieldName = fieldDefinitions.name;
            String type = fieldDefinitions.type;
            String size = fieldDefinitions.size;
            String dec = fieldDefinitions.dec;
            String format = fieldDefinitions.format;
            String ref = fieldDefinitions.ref;

            if (type.equals(Definitions.FIELDS_REG_TYPE_STRING)) fields.addField(new Field<String>(fieldName));
            if (type.equals(Definitions.FIELDS_REG_TYPE_DATE)) fields.addField(new Field<Date>(fieldName));

            if (type.equals(Definitions.FIELDS_REG_TYPE_NUMBER))
                fields.addField(dec.isEmpty() ?
                        new Field<Integer>(fieldName) :
                        new Field<Double>(fieldName));
        }

        return fields;
    }
}
