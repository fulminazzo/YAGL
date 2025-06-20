package it.fulminazzo.yagl.inventory;

import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.yagl.utils.NMSUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * An implementation of {@link AnvilInventoryWrapper} with support for Minecraft 1.12 and 1.13.
 */
class AnvilInventoryWrapper12_13 extends AnvilInventoryWrapper {

    /**
     * Instantiates a new Anvil inventory wrapper.
     *
     * @param actualInventory the actual inventory
     */
    public AnvilInventoryWrapper12_13(final @NotNull Inventory actualInventory) {
        super(actualInventory);
    }

    @Override
    public void open(@NotNull Player player) {
        final @NotNull Refl<?> entityPlayer = NMSUtils.getHandle(player);

        Refl<?> containerRefl = new Refl<>(NMSUtils.getCraftBukkitClass("inventory.CraftContainer"),
                this.actualInventory,
                entityPlayer.getObject(),
                entityPlayer.invokeMethod("nextContainerCounter")
        );

        Refl<?> containerAnvil = containerRefl.getFieldRefl("delegate");
        for (int i = 0; i < this.actualInventory.getSize(); i++) {
            ItemStack item = this.actualInventory.getItem(i);
            if (item != null)
                containerAnvil.invokeMethod("setItem", i, NMSUtils.itemStackToNMS(item));
        }

        InventoryView view = containerRefl.invokeMethod("getBukkitView");
        Object openWindowPacket = NMSUtils.newOpenWindowPacket(
                containerRefl.getObject(),
                containerRefl.getFieldObject("windowId"),
                view.getTitle(),
                this.actualInventory
        );
        NMSUtils.sendPacket(player, openWindowPacket);

        // Set fields
        entityPlayer.setFieldObject("activeContainer", containerRefl.getObject());
        containerRefl.invokeMethod("addSlotListener", entityPlayer.getObject());
    }

}
