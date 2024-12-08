package it.fulminazzo.yagl.actions;

import it.fulminazzo.yagl.GUIManager;
import it.fulminazzo.yagl.contents.GUIContent;
import it.fulminazzo.yagl.contents.ItemGUIContent;
import it.fulminazzo.yagl.guis.GUI;
import it.fulminazzo.yagl.utils.GUITestUtils;
import it.fulminazzo.yagl.viewers.Viewer;
import it.fulminazzo.jbukkit.BukkitUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
                new Object[]{(Consumer<GUI>) g -> g.addContent(new GUIContent[]{ItemGUIContent.newInstance().onClickItem(COMMAND)}),
                        (BiConsumer<Viewer, GUI>) (v, g) -> g.getContents(0).forEach(c -> c.clickItemAction().ifPresent(a -> a.execute(v, g, c)))}
        };
    }

    @ParameterizedTest
    @MethodSource("guiActions")
    void testGUICommandActions(Consumer<GUI> setupAction, BiConsumer<Viewer, GUI> runAction) {
        GUITestUtils.mockPlugin(p -> {
            Player player = BukkitUtils.addPlayer(UUID.randomUUID(), "Alex");
            when(player.isOnline()).thenReturn(true);

            GUI gui = GUI.newGUI(9);
            setupAction.accept(gui);
            runAction.accept(GUIManager.getViewer(player), gui);

            verify(Bukkit.getServer()).dispatchCommand(player, COMMAND);
        });
    }

}
