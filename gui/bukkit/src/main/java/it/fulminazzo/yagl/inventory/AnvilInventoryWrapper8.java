package it.fulminazzo.yagl.inventory;

import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.yagl.utils.NMSUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * An implementation of {@link AnvilInventoryWrapper} with support for Minecraft 1.8.
 */
class AnvilInventoryWrapper8 extends AnvilInventoryWrapper {

    /**
     * Instantiates a new Anvil inventory wrapper.
     *
     * @param actualInventory the actual inventory
     */
    public AnvilInventoryWrapper8(final @NotNull Inventory actualInventory) {
        super(actualInventory);
    }

    @Override
    public void open(@NotNull Player player) {
        final @NotNull Refl<?> entityPlayer = NMSUtils.getHandle(player);

        Refl<?> containerRefl = new Refl<>(NMSUtils.getCraftBukkitClass("inventory.CraftContainer"),
                this.actualInventory,
                player,
                entityPlayer.invokeMethod("nextContainerCounter")
        );

        Object internalInventory = new Refl<>(this.actualInventory).invokeMethod("getInventory");

        final @NotNull Class<?> slotClass = NMSUtils.getLegacyNMSClass("Slot");
        List<Object> slots = containerRefl.getFieldObject(f -> checkFieldListOf(f, slotClass));
        slots.add(new Refl<>(slotClass, internalInventory, 0, 27, 47).getObject());
        slots.add(new Refl<>(slotClass, internalInventory, 1, 76, 47).getObject());
        slots.add(new Refl<>(slotClass, internalInventory, 2, 134, 47).getObject());

        setPlayerInventorySlots(player, containerRefl.getObject());

        List<Object> itemStacks = containerRefl.getFieldObject(f ->
                checkFieldListOf(f, NMSUtils.getLegacyNMSClass("ItemStack"))
        );
        for (int i = 0; i < this.actualInventory.getSize(); i++) {
            ItemStack item = this.actualInventory.getItem(i);
            if (item != null) itemStacks.add(NMSUtils.itemStackToNMS(item));
            else itemStacks.add(null);
        }

        // Set player inventory items
        for (int i = 0; i < player.getInventory().getSize(); i++)
            itemStacks.add(null);

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

    /**
     * Sets the corresponding {@link Player#getInventory()} slots in the specified container.
     *
     * @param player    the player
     * @param container the container
     */
    static void setPlayerInventorySlots(final @NotNull Player player,
                                        final @NotNull Object container) {
        final @NotNull Class<?> slotClass = NMSUtils.getLegacyNMSClass("Slot");
        List<Object> slots = new Refl<>(container).getFieldObject(f -> checkFieldListOf(f, slotClass));

        Object bottomInventory = new Refl<>(player.getInventory()).invokeMethod("getInventory");

        // Set storage slots
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                Object slot = new Refl<>(slotClass,
                        bottomInventory,
                        col + row * 9 + 9, 8 + col * 18, 31 + row * 18
                ).getObject();
                slots.add(slot);
            }
        }

        // Set hotbar contents
        for (int col = 0; col < 9; col++) {
            Object slot = new Refl<>(slotClass,
                    bottomInventory,
                    col, 8 + col * 18, 89
            ).getObject();
            slots.add(slot);
        }
    }

    /**
     * Checks if the given field is a {@link List} containing elements of the specified class.
     *
     * @param field the field
     * @param clazz the class
     * @return true if it is
     */
    static boolean checkFieldListOf(final @NotNull Field field, final @NotNull Class<?> clazz) {
        Type type = field.getGenericType();
        if (type instanceof ParameterizedType) {
            Type parameterType = ((ParameterizedType) type).getActualTypeArguments()[0];
            return parameterType.equals(clazz);
        }
        return false;
    }

}
