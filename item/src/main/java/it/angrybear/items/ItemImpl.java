package it.angrybear.items;

import it.angrybear.structures.EnchantmentSet;
import it.fulminazzo.fulmicollection.objects.Printable;
import it.fulminazzo.fulmicollection.utils.ReflectionUtils;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@Getter
class ItemImpl implements Item {
    private String material;
    private int amount;
    private short durability;
    private String displayName;
    private final List<String> lore;
    private final Set<Enchantment> enchantments;
    private final Set<ItemFlag> itemFlags;
    private boolean unbreakable;

    public ItemImpl() {
        this(null);
    }

    public ItemImpl(final String material) {
        this(material, 1);
    }

    public ItemImpl(final String material, final int amount) {
        if (material != null) setMaterial(material);
        setAmount(amount).setDisplayName("");
        this.durability = 0;
        this.lore = new LinkedList<>();
        this.enchantments = new EnchantmentSet();
        this.itemFlags = new HashSet<>();
    }

    @Override
    public Item setMaterial(final @NotNull String material) {
        this.material = material;
        return this;
    }

    @Override
    public Item setAmount(final int amount) {
        this.amount = amount;
        return this;
    }

    @Override
    public Item setDurability(final short durability) {
        this.durability = durability;
        return this;
    }

    @Override
    public Item setDisplayName(final @NotNull String displayName) {
        this.displayName = displayName;
        return this;
    }

    @Override
    public Item setLore(final @NotNull Collection<String> lore) {
        this.lore.clear();
        this.lore.addAll(lore);
        return this;
    }

    @Override
    public Item setUnbreakable(final boolean unbreakable) {
        this.unbreakable = unbreakable;
        return this;
    }

    @Override
    public boolean isSimilar() {
        return false;
    }

    /**
     * Compare this item with the given one.
     *
     * @param item the item
     * @return true if they are equal
     */
    public boolean equals(final @Nullable Item item) {
        if (item == null) return false;
        return ReflectionUtils.equalsFields(this, item);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Item) return equals((Item) o);
        return super.equals(o);
    }

    @Override
    public String toString() {
        return Printable.convertToJson(this);
    }
}
