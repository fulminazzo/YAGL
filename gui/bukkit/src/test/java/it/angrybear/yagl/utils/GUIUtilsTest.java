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
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class GUIUtilsTest {

    @Test
    void testOpenGUI() {
        BukkitUtils.setupServer();

        Server server = Bukkit.getServer();
        when(server.getPluginManager()).thenReturn(mock(PluginManager.class));

        AtomicReference<Inventory> inventory = new AtomicReference<>();
        Player player = BukkitUtils.addPlayer(UUID.randomUUID(), "Alex");
        when(player.isOnline()).thenReturn(true);
        when(player.openInventory(any(Inventory.class)))
                .thenAnswer(a -> {
                    inventory.set(a.getArgument(0));
                    return null;
                });

        GUI expected = GUI.newGUI(9)
                .setTitle("Hello world")
                .addContent(Item.newItem("stone").setDisplayName("test"));

        try (MockedStatic<JavaPlugin> javaPlugin = mockStatic(JavaPlugin.class)) {
            when(JavaPlugin.getProvidingPlugin(any())).thenAnswer(a -> mock(JavaPlugin.class));
            GUIUtils.openGUI(expected, GUIManager.getViewer(player));
        }

        assertNotNull(inventory);
        assertEquals(expected.size(), inventory.get().getSize());
        assertEquals(expected.getTitle(), new Refl<>(inventory.get()).getFieldObject("title"));

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