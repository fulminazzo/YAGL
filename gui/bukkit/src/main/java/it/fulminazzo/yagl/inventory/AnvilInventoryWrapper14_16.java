package it.fulminazzo.yagl.inventory;

import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.yagl.utils.NMSUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

/**
 * An implementation of {@link AnvilInventoryWrapper} with support from Minecraft 1.14 to 1.16.
 */
final class AnvilInventoryWrapper14_16 extends AnvilInventoryWrapper13 {

    /**
     * Instantiates a new Anvil inventory wrapper.
     *
     * @param actualInventory the actual inventory
     */
    public AnvilInventoryWrapper14_16(final @NotNull Inventory actualInventory) {
        super(actualInventory);
    }

    @Override
    @NotNull Refl<?> getDelegateContainer(final @NotNull Player player,
                                          final @NotNull Refl<?> container) {
        final Refl<?> delegateRefl = container.getFieldRefl("delegate");

        Object world = NMSUtils.getHandle(player).getFieldObject("world");
        Object blockPosition = new Refl<>(NMSUtils.getLegacyNMSClass("BlockPosition"),
                player.getLocation().getBlockX(),
                player.getLocation().getBlockY(),
                player.getLocation().getBlockZ()
        ).getObject();

        Object containerAccess = new Refl<>(NMSUtils.getLegacyNMSClass("ContainerAccess"))
                .invokeMethod("at", world, blockPosition);

        delegateRefl.setFieldObject("containerAccess", containerAccess);
        return delegateRefl;
    }

}
