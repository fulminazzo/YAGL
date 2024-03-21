package it.angrybear.yagl.viewers;

import it.angrybear.yagl.contents.GUIContent;
import it.angrybear.yagl.guis.GUI;
import it.angrybear.yagl.items.BukkitItem;
import it.angrybear.yagl.listeners.GUIListener;
import it.angrybear.yagl.utils.GUIUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class BukkitViewer extends Viewer {

    /**
     * Instantiates a new Viewer.
     *
     * @param uniqueId the unique id
     * @param name     the name
     */
    BukkitViewer(UUID uniqueId, String name) {
        super(uniqueId, name);
    }

    @Override
    public void executeCommand(final @NotNull String command) {
        Player player = getPlayer();
        if (player == null) throw new IllegalStateException();
        Bukkit.dispatchCommand(player, command);
    }

    @Override
    public void openGUI(@NotNull GUI gui) {
        Player player = getPlayer();
        if (player == null) throw new IllegalStateException(String.format("%s is not online", this.name));
        final Inventory inventory = GUIUtils.guiToInventory(gui);
        for (int i = 0; i < gui.getSize(); i++) {
            GUIContent content = gui.getContent(this, i);
            if (content != null) {
                ItemStack o = content.render().copy(BukkitItem.class).create();
                inventory.setItem(i, o);
            }
        }
        GUIListener.getOpenGUI(this.uniqueId).ifPresent(g -> g.changeGUIAction().ifPresent(a -> {
            GUIListener.closeGUI(this.uniqueId);
            a.execute(this, g, gui);
        }));
        player.closeInventory();
        GUIListener.openGUI(this, gui);
        player.openInventory(inventory);
    }

    @Override
    public boolean hasPermission(@NotNull String permission) {
        Player player = getPlayer();
        return player != null && player.hasPermission(permission);
    }

    private Player getPlayer() {
        Player player = Bukkit.getPlayer(this.uniqueId);
        if (player == null) return null;
        else if (player.isOnline()) return player;
        else return null;
    }

    public static Viewer newViewer(final @NotNull Player player) {
        return new BukkitViewer(player.getUniqueId(), player.getName());
    }
}
