package SPEDFiscal;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

class Field<T>  {
    private final String name;
    private T value;

    protected Field(String name){
        this.name = name;
        this.value = null;
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
    private final String registerName;

    public String getRegisterName() {
        return registerName;
    }

    Fields(String registerName){
        this.registerName = registerName;
    }

    public void addField(Field<?> field) {
        this.put(field.getName(), field);
    }

    public Field<?> getField(String name){
        for (Map.Entry<String, Field<?>> e : this.entrySet()) {
            Field<?> field = e.getValue();

            if (field.getName().equals(name))
                return field;
        }

        return null;
    }
}

