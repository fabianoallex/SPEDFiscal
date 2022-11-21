package SPEDFiscalLib;

import java.util.HashMap;
import java.util.stream.Collectors;

public class Counter extends HashMap<String, Integer> {
    public void increment(String name) {
        int currVal = this.get(name) == null ? 0 : this.get(name);
        this.put(name, currVal + 1);
    }

    public int count() {
        return this.values().stream().mapToInt(Integer::intValue).sum();
    }

    public int count(String name) {
        return this.get(name) == null ? 0 : this.get(name);
    }
}