package it.fulminazzo.yagl.utils;

import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoop;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.mockito.MockedStatic;

import java.util.concurrent.Callable;
import java.util.function.Consumer;

import static org.mockito.Mockito.*;

/**
 * A collection of utilities used to test the module
 */
@NoArgsConstructor
public final class BukkitTestUtils {

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

    /**
     * Mocks the returned value of {@link NMSUtils#getPlayerChannel(Player)} and executes the given function.
     *
     * @param function the function
     */
    public static void mockNMSUtils(final @NotNull Consumer<Channel> function) {
        try (MockedStatic<NMSUtils> ignored = mockStatic(NMSUtils.class)) {
            Channel channel = mock(Channel.class);
            when(NMSUtils.getPlayerChannel(any())).thenReturn(channel);

            ChannelPipeline pipeline = mock(ChannelPipeline.class);
            when(channel.pipeline()).thenReturn(pipeline);

            EventLoop eventLoop = mock(EventLoop.class);
            when(channel.eventLoop()).thenReturn(eventLoop);
            when(eventLoop.submit(any(Callable.class))).thenAnswer(a -> {
                Callable<?> callable = a.getArgument(0);
                return callable.call();
            });

            function.accept(channel);
        }
    }

}
