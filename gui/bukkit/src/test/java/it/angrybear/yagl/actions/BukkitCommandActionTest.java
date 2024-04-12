package it.angrybear.yagl.actions;

import it.angrybear.yagl.GUIManager;
import it.angrybear.yagl.contents.GUIContent;
import it.angrybear.yagl.contents.ItemGUIContent;
import it.angrybear.yagl.guis.GUI;
import it.angrybear.yagl.viewers.Viewer;
import it.fulminazzo.jbukkit.BukkitUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.MockedStatic;

import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BukkitCommandActionTest {
    private static final String COMMAND = "/helloworld";

    @BeforeEach
    void setUp() {
        BukkitUtils.setupServer();
    }

    private static Object[][] guiActions() {
        return new Object[][]{
                new Object[]{(Consumer<GUI>) g -> g.onOpenGUI(COMMAND),
                        (BiConsumer<Viewer, GUI>) (v, g) -> g.openGUIAction().ifPresent(a -> a.execute(v, g))},
                new Object[]{(Consumer<GUI>) g -> g.onCloseGUI(COMMAND),
                        (BiConsumer<Viewer, GUI>) (v, g) -> g.closeGUIAction().ifPresent(a -> a.execute(v, g))},
                new Object[]{(Consumer<GUI>) g -> g.onChangeGUI(COMMAND),
                        (BiConsumer<Viewer, GUI>) (v, g) -> g.changeGUIAction().ifPresent(a -> a.execute(v, g, null))},
                new Object[]{(Consumer<GUI>) g -> g.onClickOutside(COMMAND),
                        (BiConsumer<Viewer, GUI>) (v, g) -> g.clickOutsideAction().ifPresent(a -> a.execute(v, g))},
                new Object[]{(Consumer<GUI>) g -> g.addContent(new GUIContent[]{new ItemGUIContent().onClickItem(COMMAND)}),
                        (BiConsumer<Viewer, GUI>) (v, g) -> g.getContents(0).forEach(c -> c.clickItemAction().ifPresent(a -> a.execute(v, g, c)))}
        };
    }

    @ParameterizedTest
    @MethodSource("guiActions")
    void testGUICommandActions(Consumer<GUI> setupAction, BiConsumer<Viewer, GUI> runAction) {
        try (MockedStatic<JavaPlugin> ignored = mockStatic(JavaPlugin.class)) {
            JavaPlugin plugin = mock(JavaPlugin.class);
            when(JavaPlugin.getProvidingPlugin(any())).thenAnswer(a -> plugin);
            when(Bukkit.getPluginManager()).thenReturn(mock(PluginManager.class));

            Player player = BukkitUtils.addPlayer(UUID.randomUUID(), "Alex");
            when(player.isOnline()).thenReturn(true);

            GUI gui = GUI.newGUI(9);
            setupAction.accept(gui);
            runAction.accept(GUIManager.getViewer(player), gui);

            verify(Bukkit.getServer()).dispatchCommand(player, COMMAND);
        }
    }

}
