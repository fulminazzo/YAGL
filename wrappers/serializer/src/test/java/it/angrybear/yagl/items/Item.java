package it.angrybear.yagl.items;

@SuppressWarnings("deprecation")
public class Item implements AbstractItem {
    private final String material;

    public Item() {
        this(null);
    }

    public Item(String material) {
        this.material = material;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Item) return this.material.equals(((Item) o).material);
        return super.equals(o);
    }
}
