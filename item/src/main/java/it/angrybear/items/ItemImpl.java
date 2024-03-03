package it.angrybear.items;

import it.angrybear.structures.EnchantmentSet;
import it.angrybear.utils.MessageUtils;
import it.fulminazzo.fulmicollection.objects.Printable;
import it.fulminazzo.fulmicollection.utils.ReflectionUtils;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * An implementation of {@link Item}.
 */
@Getter
class ItemImpl implements Item {
    private String material;
    private int amount;
    private int durability;
    private String displayName;
    private final List<String> lore;
    private final Set<Enchantment> enchantments;
    private final Set<ItemFlag> itemFlags;
    private boolean unbreakable;

    /**
     * Instantiates a new Item.
     */
    public ItemImpl() {
        this(null);
    }

    /**
     * Instantiates a new Item.
     *
     * @param material the material
     */
    public ItemImpl(final String material) {
        this(material, 1);
    }

    /**
     * Instantiates a new Item.
     *
     * @param material the material
     * @param amount   the amount
     */
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
    public Item setDurability(final int durability) {
        this.durability = durability;
        return this;
    }

    @Override
    public Item setDisplayName(final @NotNull String displayName) {
        this.displayName = MessageUtils.color(displayName);
        return this;
    }

    @Override
    public Item setUnbreakable(final boolean unbreakable) {
        this.unbreakable = unbreakable;
        return this;
    }

    @Override
    public boolean isSimilar(final @Nullable Item item, final ItemField @NotNull ... ignore) {
        if (item == null) return false;
        mainloop:
        for (final Field field : ItemImpl.class.getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers())) continue;
            try {
                for (final ItemField f : ignore)
                    if (field.getName().equalsIgnoreCase(f.name().replace("_", "")))
                        continue mainloop;
                field.setAccessible(true);
                Object obj1 = field.get(this);
                Object obj2 = field.get(item);
                if (!Objects.equals(obj1, obj2)) return false;
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return true;
    }

    @Override
    public Item copy() {
        ItemImpl item = new ItemImpl();
        for (final Field field : ItemImpl.class.getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers())) continue;
            try {
                field.setAccessible(true);
                Object obj1 = field.get(this);
                field.set(item, obj1);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return item;
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
