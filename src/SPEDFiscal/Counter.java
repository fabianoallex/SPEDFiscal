package SPEDFiscal;

import java.util.ArrayList;

public class Counter {
    ArrayList <ItemCount> list;

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
        ItemCount itemCount = null;

        for (ItemCount i : this.list)
            if (i.name.equals(name)) {
                itemCount = i;
                break;
            }

        if (itemCount == null) itemCount = this.add(name);

        itemCount.increment();
    }

    int getCount(String name){
        ItemCount itemCount = null;

        for (ItemCount i : this.list)
            if (i.name.equals(name)) {
                itemCount = i;
                break;
            }

        if (itemCount == null) return 0;

        return itemCount.count;
    }

    int getCount() {
        int c = 0;
        for (ItemCount i : this.list) c += i.getCount();
        return c;
    }
}

class ItemCount {
    int count;
    String name;

    ItemCount(String name){
        this.name = name;
        count = 0;
    }

    public String getName() {
        return name;
    }

    void increment(){
        count++;
    }

    public int getCount() {
        return count;
    }
}