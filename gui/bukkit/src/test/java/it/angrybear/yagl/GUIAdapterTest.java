package it.angrybear.yagl;

import it.angrybear.yagl.contents.GUIContent;
import it.angrybear.yagl.contents.ItemGUIContent;
import it.angrybear.yagl.guis.GUI;
import it.angrybear.yagl.guis.GUIType;
import it.angrybear.yagl.items.Item;
import it.angrybear.yagl.utils.GUITestUtils;
import it.angrybear.yagl.viewers.PlayerOfflineException;
import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.jbukkit.BukkitUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GUIAdapterTest {
    private Player player;
    private Inventory inventory;

    @BeforeEach
    void setUp() {
        BukkitUtils.setupServer();

        Server server = Bukkit.getServer();
        when(server.getPluginManager()).thenReturn(mock(PluginManager.class));

        this.player = BukkitUtils.addPlayer(UUID.randomUUID(), "Alex");
        when(this.player.isOnline()).thenReturn(true);
        when(this.player.openInventory(any(Inventory.class)))
                .thenAnswer(a -> {
                    this.inventory = a.getArgument(0);
                    return null;
                });
    }

    private static GUI[] guis() {
        return new GUI[]{
                GUI.newGUI(9)
                        .setTitle("Hello world")
                        .addContent(Item.newItem("stone").setDisplayName("test")),
                GUI.newGUI(9)
                        .setTitle(null)
                        .addContent(Item.newItem("stone").setDisplayName("test")),
                GUI.newGUI(GUIType.CHEST)
                        .setTitle("Hello world")
                        .addContent(Item.newItem("stone").setDisplayName("test")),
                GUI.newGUI(GUIType.CHEST)
                        .setTitle(null)
                        .addContent(Item.newItem("stone").setDisplayName("test")),
        };
    }

    @ParameterizedTest
    @MethodSource("guis")
    void testOpenGUI(GUI expected) {
        openGUI(expected);

        assertNotNull(this.inventory);
        assertEquals(expected.size(), this.inventory.getSize());
        assertEquals(expected.getTitle(), new Refl<>(this.inventory).getFieldObject("title"));

        for (int i = 0; i < expected.size(); i++) {
            @NotNull List<GUIContent> contents = expected.getContents(i);
            GUIContent guiContent = contents.isEmpty() ? null : contents.get(0);
            ItemStack actual = this.inventory.getItem(i);
            if (guiContent == null) assertNull(actual);
            else {
                Item item = new Refl<>(guiContent).getFieldObject("item");
                ItemStack exp = ItemAdapter.itemToItemStack(item);
                assertEquals(exp, actual);
            }
        }
    }

    @Test
    void testOpenGUIMeta() {
        GUI gui = GUI.newGUI(GUIType.CHEST)
                .setTitle(null)
                .addContent(Item.newItem(Material.PLAYER_HEAD.name()).setDisplayName("test"));
        GUITestUtils.mockPlugin(p -> GUIAdapter.openGUI(gui, GUIManager.getViewer(this.player), m -> m.setUnbreakable(true)));

        assertNotNull(this.inventory);

        ItemStack expected = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta meta = expected.getItemMeta();
        meta.setDisplayName("test");
        meta.setUnbreakable(true);
        expected.setItemMeta(meta);

        assertEquals(expected, this.inventory.getItem(0));
    }

    @Test
    void testOpenGUIRequirements() {
        when(this.player.hasPermission(anyString())).thenReturn(false);

        GUI expected = GUI.newGUI(9)
                .setTitle("Hello world")
                .addContent(ItemGUIContent.newInstance("stone").setViewRequirements(v -> false),
                        ItemGUIContent.newInstance("diamond").setViewRequirements("permission"));
        openGUI(expected);

        assertNotNull(this.inventory);
        assertEquals(expected.size(), this.inventory.getSize());
        assertEquals(expected.getTitle(), new Refl<>(this.inventory).getFieldObject("title"));

        for (int i = 0; i < expected.size(); i++)
            assertNull(this.inventory.getItem(i));
    }

    @Test
    void testOpenGUIForOfflinePlayer() {
        BukkitUtils.removePlayer(this.player);
        assertThrowsExactly(PlayerOfflineException.class, () -> openGUI(GUI.newGUI(9)));
    }

    private void openGUI(GUI gui) {
        GUITestUtils.mockPlugin(p -> gui.open(GUIManager.getViewer(this.player)));
    }

}