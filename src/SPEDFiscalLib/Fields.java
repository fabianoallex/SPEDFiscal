package SPEDFiscalLib;

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
}

