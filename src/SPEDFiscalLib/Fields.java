package SPEDFiscalLib;

import java.util.LinkedHashMap;
import java.util.Map;

class Field<T>  {
    private final String name;
    private T value;
    private String required;

    protected Field(String name){
        this.name = name;
        this.value = null;
    }

    protected Field(String name, String required){
        this.name = name;
        this.value = null;
        this.required = required;
    }

    protected Field(){
        this.name = "";
        this.value = null;
    }

    public String getRequired() {
        return required;
    }

    public void setRequired(String required) {
        this.required = required;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public String getName(){
        return this.name;
    }

    public T getValue(){
        return value;
    }
}

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
}

