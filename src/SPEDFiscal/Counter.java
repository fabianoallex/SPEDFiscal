package SPEDFiscal;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Counter extends HashMap<String, Integer> {
    public void increment(String name) {
        int currVal = this.get(name) == null ? 0 : this.get(name);
        this.put(name, currVal + 1);
    }

    public int sum() {
        int total = 0;
        for (Entry<String, Integer> entry : this.entrySet()) {
            Integer count = entry.getValue();
            total += count;
        }
        return total;
    }
}