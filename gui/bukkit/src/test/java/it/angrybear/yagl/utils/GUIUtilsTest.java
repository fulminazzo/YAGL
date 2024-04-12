package it.angrybear.yagl.utils;

import it.angrybear.yagl.GUIManager;
import it.angrybear.yagl.ItemAdapter;
import it.angrybear.yagl.contents.GUIContent;
import it.angrybear.yagl.guis.GUI;
import it.angrybear.yagl.items.Item;
import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.jbukkit.BukkitUtils;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.Test;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GUIUtilsTest {

    @Test
    void testOpenGUI() {
        BukkitUtils.setupServer();

        AtomicReference<Inventory> inventory = new AtomicReference<>();
        Player player = mock(Player.class);
                when(player.isOnline()).thenReturn(true);
        when(player.openInventory(any(Inventory.class)))
                .thenAnswer(a -> {
                    inventory.set(a.getArgument(0));
                    return null;
                });

        Server server = Bukkit.getServer();
        when(server.getPlayer(any(UUID.class))).thenReturn(player);

        GUI expected = GUI.newGUI(9)
                .setTitle("Hello world")
                .addContent(Item.newItem("stone").setDisplayName("test"));

        GUIUtils.openGUI(expected, GUIManager.getViewer(player));

        assertNotNull(inventory);
        assertEquals(expected.size(), inventory.get().getSize());
        assertEquals(expected.getTitle(), new Refl<>(inventory).getFieldObject("title"));

        for (int i = 0; i < expected.size(); i++) {
            GUIContent guiContent = expected.getContents(i).get(0);
            ItemStack actual = inventory.get().getItem(i);
            if (guiContent == null) assertNull(actual);
            else {
                Item item = new Refl<>(guiContent).getFieldObject("item");
                ItemStack exp = ItemAdapter.itemToItemStack(item);
                assertEquals(exp, actual);
            }
        }
    }

}