package it.angrybear.items;

import it.angrybear.utils.MaterialUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * An implementation of {@link BukkitItem}.
 */
class BukkitItemImpl extends ItemImpl implements BukkitItem {

    @Override
    public BukkitItemImpl setMaterial(@NotNull String material) {
        MaterialUtils.getMaterial(material, true);
        return (BukkitItemImpl) super.setMaterial(material);
    }

    @Override
    public BukkitItemImpl setAmount(final int amount) {
        return (BukkitItemImpl) super.setAmount(amount);
    }

    @Override
    public BukkitItemImpl setDurability(final int durability) {
        return (BukkitItemImpl) super.setDurability(durability);
    }

    @Override
    public BukkitItemImpl setDisplayName(final @NotNull String displayName) {
        return (BukkitItemImpl) super.setDisplayName(displayName);
    }

    @Override
    public BukkitItemImpl setLore(final @NotNull Collection<String> lore) {
        return (BukkitItemImpl) super.setLore(lore);
    }

    @Override
    public BukkitItemImpl setUnbreakable(final boolean unbreakable) {
        return (BukkitItemImpl) super.setUnbreakable(unbreakable);
    }

    @Override
    public BukkitItemImpl copy() {
        return super.copy(BukkitItemImpl.class);
    }
}
