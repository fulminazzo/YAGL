package it.angrybear.yagl.utils;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.mockito.MockedStatic;

import java.util.function.Consumer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * A collection of utilities used to test the module
 */
public class GUITestUtils {

    /**
     * Mocks the returned value of {@link JavaPlugin#getProvidingPlugin(Class)} and executes the given function.
     *
     * @param function the function
     */
    public static void mockPlugin(final @NotNull Consumer<Plugin> function) {
        try (MockedStatic<JavaPlugin> ignored = mockStatic(JavaPlugin.class)) {
            JavaPlugin plugin = mock(JavaPlugin.class);
            when(JavaPlugin.getProvidingPlugin(any())).thenAnswer(a -> plugin);
            when(Bukkit.getPluginManager()).thenReturn(mock(PluginManager.class));

            function.accept(plugin);
        }
    }
}
