package it.fulminazzo.yagl.item;

import it.fulminazzo.yagl.Copiable;
import it.fulminazzo.yagl.item.field.ItemField;
import it.fulminazzo.yagl.item.field.ItemFlag;
import it.fulminazzo.yagl.reflect.Reflect;
import it.fulminazzo.yagl.structure.EnchantmentSet;
import it.fulminazzo.yagl.util.MessageUtils;
import it.fulminazzo.yagl.util.ValidationUtils;
import it.fulminazzo.yagl.wrapper.Enchantment;
import it.fulminazzo.yagl.wrapper.PotionEffect;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.*;

/**
 * An implementation of {@link Item}.
 */
@Getter
@EqualsAndHashCode(callSuper = false)
class ItemImpl extends Copiable<Item> implements Item {
    protected String material;
    @Range(from = Constants.MIN_AMOUNT, to = Integer.MAX_VALUE)
    protected int amount = Constants.MIN_AMOUNT;
    @Range(from = Constants.MIN_DURABILITY, to = Short.MAX_VALUE)
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
    public ItemImpl(final @Nullable String material) {
        this(material, Constants.MIN_AMOUNT);
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
        this.durability = Constants.MIN_DURABILITY;
        this.lore = new ArrayList<>();
        this.enchantments = new EnchantmentSet();
        this.itemFlags = new HashSet<>();
        this.potionEffects = new HashSet<>();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected @NotNull <T extends Item> T copyDataTo(final @NotNull T copy) {
        if (material != null) copy.setMaterial(material);
        return (T) copy
                .setAmount(amount)
                .setDurability(durability)
                .setDisplayName(displayName)
                .setLore(lore)
                .addEnchantments(enchantments)
                .addItemFlags(itemFlags)
                .setPotionEffects(potionEffects)
                .setUnbreakable(unbreakable)
                .setCustomModelData(customModelData);
    }

    @Override
    public @NotNull Item setMaterial(final @NotNull String material) {
        this.material = material;
        return this;
    }

    @Override
    public @NotNull Item setAmount(final @Range(from = Constants.MIN_AMOUNT, to = Integer.MAX_VALUE) int amount) {
        ValidationUtils.checkMin("amount", amount, Constants.MIN_AMOUNT);
        this.amount = amount;
        return this;
    }

    @Override
    public @NotNull Item setDurability(final @Range(from = Constants.MIN_DURABILITY, to = Short.MAX_VALUE) int durability) {
        ValidationUtils.checkMin("durability", durability, Constants.MIN_DURABILITY);
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
    public @NotNull Item setCustomModelData(final int customModelData) {
        this.customModelData = customModelData;
        return this;
    }

    @Override
    public boolean isSimilar(final @Nullable Item item, final ItemField @NotNull ... ignore) {
        Reflect curr = Reflect.on(this);
        Reflect other = Reflect.on(item);
        return item != null && curr.getNonStaticFields().stream()
                .filter(f -> Arrays.stream(ignore)
                        .noneMatch(f2 -> f.getName().equalsIgnoreCase(f2.name()
                                .replace("_", ""))))
                .allMatch(f -> Objects.equals(curr.get(f), other.get(f)));
    }

    @Override
    public @NotNull String toString() {
        StringBuilder builder = new StringBuilder()
                .append(getClass().getSimpleName().replace("Impl", ""))
                .append("{")
                .append(material)
                .append(" x ")
                .append(amount);
        if (displayName != null && !displayName.isEmpty())
            builder.append(", '").append(displayName).append("'");
        return builder.append("}").toString();
    }

}
