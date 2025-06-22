package it.fulminazzo.yagl.utils;

import it.fulminazzo.jbukkit.inventory.MockInventoryView;
import lombok.Getter;
import net.minecraft.server.v1_14_4_R1.Container;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

@Getter
public class Legacy14MockInventoryView extends MockInventoryView {
    private final Container container;

    /**
     * Instantiates a new Mock inventory view.
     *
     * @param topInventory the inventory displayed
     * @param player       the player viewing it
     * @param title        the title
     */
    public Legacy14MockInventoryView(@NotNull Inventory topInventory,
                                     @NotNull Player player,
                                     @NotNull String title,
                                     @NotNull Container container) {
        super(topInventory, player, title);
        this.container = container;
    }

}
