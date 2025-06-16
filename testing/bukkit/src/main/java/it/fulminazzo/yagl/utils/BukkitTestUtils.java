package it.fulminazzo.yagl.utils;

import io.netty.channel.*;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.mockito.MockedStatic;

import java.util.LinkedList;
import java.util.concurrent.Callable;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.logging.Logger;

import static org.mockito.Mockito.*;

/**
 * A collection of utilities used to test the module
 */
@NoArgsConstructor
public final class BukkitTestUtils {

    /**
     * Mocks the returned value of {@link JavaPlugin#getProvidingPlugin(Class)}
     * and the returned value of {@link NMSUtils#getPlayerChannel(Player)}
     * and executes the given function.
     *
     * @param function the function
     */
    public static void mockPluginAndNMSUtils(final @NotNull BiConsumer<Plugin, Channel> function) {
        try (MockedStatic<JavaPlugin> ignored = mockStatic(JavaPlugin.class)) {
            JavaPlugin plugin = mock(JavaPlugin.class);
            when(JavaPlugin.getProvidingPlugin(any())).thenAnswer(a -> plugin);
            when(Bukkit.getPluginManager()).thenReturn(mock(PluginManager.class));
            when(plugin.getLogger()).thenReturn(Logger.getLogger(BukkitTestUtils.class.getName()));

            mockNMSUtils(c -> function.accept(plugin, c));
        }
    }

    /**
     * Mocks the returned value of {@link JavaPlugin#getProvidingPlugin(Class)} and executes the given function.
     *
     * @param function the function
     */
    public static void mockPlugin(final @NotNull Consumer<Plugin> function) {
        mockPluginAndNMSUtils((p, c) -> function.accept(p));
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
            LinkedList<ChannelDuplexHandler> handlers = new LinkedList<>();
            when(channel.pipeline()).thenReturn(pipeline);
            when(pipeline.addBefore(any(), any(), any())).thenAnswer(a -> {
                ChannelHandler handler = a.getArgument(2);
                handlers.addFirst((ChannelDuplexHandler) handler);
                return null;
            });
            when(pipeline.fireChannelRead(any())).thenAnswer(a -> {
                handlers.forEach(h -> {
                    try {
                        h.channelRead(mock(ChannelHandlerContext.class), a.getArgument(0));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
                return null;
            });

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
