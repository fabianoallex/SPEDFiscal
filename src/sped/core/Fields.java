package sped.core;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class Fields extends LinkedHashMap<String, Field<?>> {
    public void addField(Field<?> field) {
        this.put(field.getName(), field);
    }

    public Field<?> getField(String name){
        for (Map.Entry<String, Field<?>> e : this.entrySet()) {
            Field<?> field = e.getValue();
            if (field.getName().equals(name)) return field;
        }

        return null;
    }

    public static Fields create(Context context, String registerName){
        Fields fields = new Fields();

        for (FieldDefinitions fieldDefinitions : context.getDefinitions().getFieldsDefinitions(registerName)) {
            String fieldName = fieldDefinitions.name;
            String type = fieldDefinitions.type;
            String dec = fieldDefinitions.dec;
            String required = fieldDefinitions.required;

            if (type.equals(Definitions.FIELDS_REG_TYPE_STRING)) fields.addField(new Field<String>(fieldName, required));
            if (type.equals(Definitions.FIELDS_REG_TYPE_DATE)) fields.addField(new Field<Date>(fieldName, required));
            if (type.equals(Definitions.FIELDS_REG_TYPE_NUMBER))
                fields.addField(dec.isEmpty() ?
                        new Field<Integer>(fieldName, required) :
                        new Field<Double>(fieldName, required));
        }

        return fields;
    }
}

