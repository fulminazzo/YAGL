package it.fulminazzo.yagl.item;

import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.yagl.util.EnumUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.Collection;
import java.util.function.Consumer;

/**
 * An implementation of {@link BukkitItem}.
 */
class BukkitItemImpl extends ItemImpl implements BukkitItem {
    private @Nullable Class<? extends ItemMeta> itemMetaClass;
    private @Nullable Consumer<? extends ItemMeta> metaFunction;

    /**
     * Instantiates a new Bukkit item.
     */
    public BukkitItemImpl() {
        super();
    }

    /**
     * Instantiates a new Bukkit item.
     *
     * @param material the material
     * @param amount   the amount
     */
    public BukkitItemImpl(final String material, final int amount) {
        super(material, amount);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected @NotNull <T extends Item> T copyDataTo(final @NotNull T copy) {
        if (copy instanceof BukkitItem) {
            Class<ItemMeta> itemMetaClass = (Class<ItemMeta>) this.itemMetaClass;
            Consumer<ItemMeta> metaFunction = (Consumer<ItemMeta>) this.metaFunction;
            ((BukkitItem) copy).setMetadata(itemMetaClass, metaFunction);
        }
        return super.copyDataTo(copy);
    }

    @Override
    public @NotNull BukkitItem copy() {
        return (BukkitItem) super.copy();
    }

    @Override
    public @NotNull ItemStack create() {
        return new Refl<>(this).invokeMethod("create", this.itemMetaClass, this.metaFunction);
    }

    @Override
    public @NotNull <M extends ItemMeta> BukkitItem setMetadata(final @Nullable Class<M> itemMetaClass,
                                                                final @Nullable Consumer<M> metaFunction) {
        this.itemMetaClass = itemMetaClass;
        this.metaFunction = metaFunction;
        return this;
    }

    @Override
    public @NotNull BukkitItem setMaterial(final @NotNull String material) {
        EnumUtils.valueOf(Material.class, material);
        return (BukkitItem) super.setMaterial(material);
    }

    @Override
    public @NotNull BukkitItem setAmount(final @Range(from = Constants.MIN_AMOUNT, to = Integer.MAX_VALUE) int amount) {
        return (BukkitItem) super.setAmount(amount);
    }

    @Override
    public @Range(from = Constants.MIN_AMOUNT, to = Integer.MAX_VALUE) int getAmount() {
        return super.getAmount();
    }

    @Override
    public @NotNull BukkitItem setDurability(final @Range(from = Constants.MIN_DURABILITY, to = Short.MAX_VALUE) int durability) {
        return (BukkitItem) super.setDurability(durability);
    }

    @Override
    public @Range(from = Constants.MIN_DURABILITY, to = Short.MAX_VALUE) int getDurability() {
        return super.getDurability();
    }

    @Override
    public @NotNull BukkitItem setDisplayName(final @NotNull String displayName) {
        return (BukkitItem) super.setDisplayName(displayName);
    }

    @Override
    public @NotNull BukkitItem setLore(final @NotNull Collection<String> lore) {
        return (BukkitItem) super.setLore(lore);
    }

    @Override
    public @NotNull BukkitItem setUnbreakable(final boolean unbreakable) {
        return (BukkitItem) super.setUnbreakable(unbreakable);
    }

    @Override
    public @NotNull BukkitItem setCustomModelData(final int customModelData) {
        return (BukkitItem) super.setCustomModelData(customModelData);
    }

}
