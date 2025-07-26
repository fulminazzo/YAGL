package it.fulminazzo.yagl.item;

import it.fulminazzo.yagl.item.field.ItemField;
import it.fulminazzo.yagl.item.field.ItemFlag;
import it.fulminazzo.yagl.structure.EnchantmentSet;
import it.fulminazzo.yagl.util.MessageUtils;
import it.fulminazzo.fulmicollection.utils.ObjectUtils;
import it.fulminazzo.yagl.wrapper.Enchantment;
import it.fulminazzo.fulmicollection.objects.FieldEquable;
import it.fulminazzo.fulmicollection.utils.ReflectionUtils;
import it.fulminazzo.yagl.wrapper.PotionEffect;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Modifier;
import java.util.*;

/**
 * An implementation of {@link Item}.
 */
@Getter
class ItemImpl extends FieldEquable implements Item {
    protected String material;
    protected int amount;
    protected int durability;
    protected String displayName;
    protected final @NotNull List<String> lore;
    protected final @NotNull Set<Enchantment> enchantments;
    protected final @NotNull Set<ItemFlag> itemFlags;
    protected final @NotNull Set<PotionEffect> potionEffects;
    protected boolean unbreakable;
    protected int customModelData;

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
    public ItemImpl(final @Nullable String material, final int amount) {
        if (material != null) setMaterial(material);
        setAmount(amount).setDisplayName("");
        this.durability = 0;
        this.lore = new LinkedList<>();
        this.enchantments = new EnchantmentSet();
        this.itemFlags = new HashSet<>();
        this.potionEffects = new HashSet<>();
    }

    @Override
    public @NotNull Item setMaterial(final @NotNull String material) {
        this.material = material;
        return this;
    }

    @Override
    public @NotNull Item setAmount(final int amount) {
        this.amount = amount;
        return this;
    }

    @Override
    public @NotNull Item setDurability(final int durability) {
        this.durability = durability;
        return this;
    }

    @Override
    public @NotNull Item setDisplayName(final @NotNull String displayName) {
        this.displayName = MessageUtils.color(displayName);
        return this;
    }

    @Override
    public @NotNull Item setUnbreakable(final boolean unbreakable) {
        this.unbreakable = unbreakable;
        return this;
    }

    @Override
    public @NotNull Item setCustomModelData(int customModelData) {
        this.customModelData = customModelData;
        return this;
    }

    @Override
    public boolean isSimilar(final @Nullable Item item, final ItemField @NotNull ... ignore) {
        return item != null && Arrays.stream(ItemImpl.class.getDeclaredFields())
                .filter(f ->! Modifier.isStatic(f.getModifiers()))
                .filter(f -> Arrays.stream(ignore)
                        .noneMatch(f2 -> f.getName().equalsIgnoreCase(f2.name()
                                .replace("_", ""))))
                .allMatch(f -> {
                    Object obj1 = ReflectionUtils.getOrThrow(f, this);
                    Object obj2 = ReflectionUtils.getOrThrow(f, item);
                    return Objects.equals(obj1, obj2);
                });
    }

    @Override
    protected @NotNull Class<? extends FieldEquable> clazz() {
        return ItemImpl.class;
    }

    @Override
    public @NotNull String toString() {
        return ObjectUtils.printAsJSON(this);
    }
}
