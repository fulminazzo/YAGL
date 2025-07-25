package it.fulminazzo.yagl.util;

import it.fulminazzo.jbukkit.inventory.MockInventoryView;
import net.minecraft.server.v1_14_R1.Container;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

@Getter
public class LegacyMockInventoryView extends MockInventoryView {
    private final Container container;

    /**
     * Instantiates a new Mock inventory view.
     *
     * @param topInventory the inventory displayed
     * @param player       the player viewing it
     * @param title        the title
     */
    public LegacyMockInventoryView(@NotNull Inventory topInventory,
                                   @NotNull Player player,
                                   @NotNull String title,
                                   @NotNull Container container) {
        super(topInventory, player, title);
        this.container = container;
    }

}
