package it.angrybear.items;

import it.angrybear.structures.EnchantmentSet;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

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
}
