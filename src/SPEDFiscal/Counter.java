package SPEDFiscal;

import java.util.ArrayList;

public class Counter {
    private ArrayList <ItemCount> list;

    Counter() {
        list = new ArrayList<>();
    }

    private void add(ItemCount itemCount){
        list.add(itemCount);
    }

    private ItemCount add(String name) {
        ItemCount itemCount = new ItemCount(name);
        list.add(itemCount);
        return itemCount;
    }

    public ArrayList <ItemCount> getArrayList(){
        return list;
    }

    public void increment(String name){
        for (ItemCount itemCount : this.list)
            if (itemCount.getName().equals(name)) {
                itemCount.increment();
                return;
            }

        this.add(name).increment();
    }

    public int getCount(String name){
        for (ItemCount itemCount : this.list)
            if (itemCount.getName().equals(name))
                return itemCount.getCount();

        return 0;
    }

    public int getCount() {
        int c = 0;
        for (ItemCount itemCount : this.list)
            c += itemCount.getCount();

        return c;
    }
}

class ItemCount {
    private int count;
    private String name;

    ItemCount(String name){
        this.name = name;
        count = 0;
    }

    public String getName() {
        return name;
    }

    public void increment(){
        count++;
    }

    public int getCount() {
        return count;
    }
}