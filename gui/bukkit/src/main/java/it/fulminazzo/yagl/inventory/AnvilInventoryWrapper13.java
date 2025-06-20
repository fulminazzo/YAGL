package it.fulminazzo.yagl.inventory;

import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.yagl.guis.SearchGUI;
import it.fulminazzo.yagl.utils.NMSUtils;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

/**
 * An implementation of {@link AnvilInventoryWrapper} with support for Minecraft 1.13.
 */
class AnvilInventoryWrapper13 extends AnvilInventoryWrapper12 {

    /**
     * Instantiates a new Anvil inventory wrapper.
     *
     * @param actualInventory the actual inventory
     */
    public AnvilInventoryWrapper13(final @NotNull Inventory actualInventory) {
        super(actualInventory);
    }

    @Override
    @NotNull Object itemStackToNMSItem(ItemStack item) {
        Object nmsItem = super.itemStackToNMSItem(item);
        Refl<?> nmsStack = new Refl<>(nmsItem);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            String name = meta.getDisplayName();
            // If the item has EMPTY_RENAME_TEXT as name, set an empty name instead.
            if (SearchGUI.EMPTY_RENAME_TEXT.equals(name)) {
                Refl<?> tagName = new Refl<>(NMSUtils.getLegacyNMSClass("NBTTagString"), "{\"text\":\"\"}");

                Refl<?> tagDisplay = new Refl<>(NMSUtils.getLegacyNMSClass("NBTTagCompound"), new Object[0])
                        .callMethod("set", "Name", tagName.getObject());

                nmsStack.invokeMethodRefl("getOrCreateTag")
                        .invokeMethod("set", "display", tagDisplay.getObject());
            }
        }
        return nmsItem;
    }

}
