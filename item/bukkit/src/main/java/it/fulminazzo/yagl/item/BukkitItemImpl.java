package it.fulminazzo.yagl.item;

import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.yagl.util.EnumUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    public BukkitItemImpl(String material, int amount) {
        super(material, amount);
    }

    @Override
    public @NotNull ItemStack create() {
        return new Refl<>(this).invokeMethod("create", this.itemMetaClass, this.metaFunction);
    }

    @Override
    public @NotNull <M extends ItemMeta> BukkitItem setMetadata(@Nullable Class<M> itemMetaClass, @Nullable Consumer<M> metaFunction) {
        this.itemMetaClass = itemMetaClass;
        this.metaFunction = metaFunction;
        return this;
    }

    @Override
    public @NotNull BukkitItem setMaterial(@NotNull String material) {
        EnumUtils.valueOf(Material.class, material);
        return (BukkitItem) super.setMaterial(material);
    }

    @Override
    public @NotNull BukkitItem setAmount(final int amount) {
        return (BukkitItem) super.setAmount(amount);
    }

    @Override
    public @NotNull BukkitItem setDurability(final int durability) {
        return (BukkitItem) super.setDurability(durability);
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
    public @NotNull BukkitItem setCustomModelData(int customModelData) {
        return (BukkitItem) super.setCustomModelData(customModelData);
    }

    @Override
    public @NotNull BukkitItem copy() {
        return super.copy(BukkitItem.class);
    }
}
