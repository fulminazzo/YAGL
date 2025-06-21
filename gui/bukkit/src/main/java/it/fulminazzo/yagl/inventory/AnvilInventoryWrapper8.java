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

        for (int i = 0; i < this.actualInventory.getSize(); i++) {
            ItemStack item = this.actualInventory.getItem(i);
            if (item != null)
                containerRefl.invokeMethod("setItem", i, NMSUtils.itemStackToNMS(item));
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
