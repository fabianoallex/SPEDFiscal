package SPEDFiscalLib;

import java.util.HashMap;

public class Counter extends HashMap<String, Integer> {
    public void increment(String name) {
        int currVal = this.get(name) == null ? 0 : this.get(name);
        this.put(name, currVal + 1);
    }

    public int count() {
        int total = 0;
        for (Entry<String, Integer> entry : this.entrySet()) {
            Integer count = entry.getValue();
            total += count;
        }
        return total;
    }

    public int count(String name) {
        return this.get(name) == null ? 0 : this.get(name);
    }
}