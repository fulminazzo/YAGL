package it.fulminazzo.yagl.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.jbukkit.BukkitUtils;
import it.fulminazzo.yagl.TestUtils;
import it.fulminazzo.yagl.scheduler.Task;
import it.fulminazzo.yagl.utils.BukkitTestUtils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AnvilRenameHandlerTest {

    private Player player;
    private ChannelHandlerContext context;

    private String lastRead;

    private AnvilRenameHandler handler;

    @BeforeAll
    static void setUpAll() {
        BukkitUtils.setupServer();
        Server server = Bukkit.getServer();
        BukkitScheduler scheduler = mock(BukkitScheduler.class);
        when(server.getScheduler()).thenReturn(scheduler);
        when(scheduler.runTaskAsynchronously(any(), any(Runnable.class))).thenAnswer(a -> {
            Runnable runnable = a.getArgument(1);
            runnable.run();
            return null;
        });
        when(scheduler.runTaskLaterAsynchronously(any(), any(Runnable.class), any(long.class))).thenAnswer(a -> {
            Runnable runnable = a.getArgument(1);
            runnable.run();
            return null;
        });
    }

    @BeforeEach
    void setUp() {
        this.player = BukkitUtils.addPlayer(UUID.randomUUID(), "fulminazzo");
        this.context = mock(ChannelHandlerContext.class);

        BukkitTestUtils.mockPlugin(p ->
                this.handler = new AnvilRenameHandler(
                        this.player.getUniqueId(),
                        (p2, n) -> this.lastRead = n
                )
        );
    }

    @Test
    void testInject() {
        BukkitTestUtils.mockNMSUtils(c -> {
            this.handler.inject();

            verify(c.pipeline()).addBefore(
                    "packet_handler",
                    AnvilRenameHandler.class.getSimpleName() +
                            "-" +
                            this.player.getUniqueId().toString().replace("-", "_"),
                    this.handler
            );
        });
    }

    @Test
    void testRemove() {
        TestUtils.disableFoliaRegionScheduler(() ->
                BukkitTestUtils.mockNMSUtils(c -> {
                    Task task = mock(Task.class);
                    Refl<AnvilRenameHandler> handler = new Refl<>(this.handler);
                    handler.setFieldObject("handleTask", task);

                    this.handler.remove();

                    verify(c.pipeline()).remove(
                            AnvilRenameHandler.class.getSimpleName() +
                                    "-" +
                                    this.player.getUniqueId().toString().replace("-", "_")
                    );
                    assertNull(handler.getFieldObject("handleTask"));
                })
        );
    }

    @Test
    void testObsoleteRead() {
        TestUtils.disableFoliaRegionScheduler(() ->
                BukkitTestUtils.mockPlugin(p -> {
                    try {
                        String expected = "Hello, world!";

                        Object packet = new PacketPlayInCustomPayload("MC|ItemName", expected);

                        this.handler.channelRead(this.context, packet);

                        assertEquals(expected, this.lastRead);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
        );
    }

    @Test
    void testObsoleteReadOfOtherPacketDoesNotCallAction() throws Exception {
        Object packet = new PacketPlayInCustomPayload("SomethingElse", "Hello, world!");

        this.handler.channelRead(this.context, packet);

        assertNull(this.lastRead);
    }

    @Test
    void testLegacyRead() {
        TestUtils.disableFoliaRegionScheduler(() ->
                BukkitTestUtils.mockPlugin(p -> {
                    try {
                        String expected = "Hello, world!";

                        Object packet = new PacketPlayInItemName(expected);

                        this.handler.channelRead(this.context, packet);

                        assertEquals(expected, this.lastRead);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
        );
    }

    @Test
    void testRead() {
        TestUtils.disableFoliaRegionScheduler(() ->
                BukkitTestUtils.mockPlugin(p -> {
                    try {
                        String expected = "Hello, world!";

                        Object packet = new ServerboundRenameItemPacket(expected);

                        this.handler.channelRead(this.context, packet);

                        assertEquals(expected, this.lastRead);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
        );
    }

    @Test
    void testReadOfOtherPacketDoesNotCallAction() throws Exception {
        this.handler.channelRead(this.context, new Object());

        assertNull(this.lastRead);
    }

    @Test
    void testExceptionDoesNotThrow() {
        assertDoesNotThrow(() -> this.handler.channelRead(this.context, null));
    }

    @Test
    void testGetPlayerOfOfflineThrows() {
        Player player = mock(Player.class);
        when(player.getUniqueId()).thenReturn(UUID.randomUUID());
        BukkitTestUtils.mockPlugin(p ->
                assertThrowsExactly(IllegalStateException.class, () ->
                        new AnvilRenameHandler(player.getUniqueId(), null).getPlayer())
        );
    }

    @Getter
    static class PacketPlayInCustomPayload {
        private final @NotNull String name;
        private final @NotNull PacketSerializer b;

        PacketPlayInCustomPayload(@NotNull String name, @NotNull String data) {
            this.name = name;
            this.b = new PacketSerializer(data);
        }


        @Getter
        static class PacketSerializer {
            private final @NotNull ByteBuf buffer;

            PacketSerializer(@NotNull String data) {
                byte[] actualData = new byte[data.length() + 1];
                for (int i = 0; i < data.length(); i++)
                    actualData[i + 1] = (byte) data.charAt(i);
                actualData[0] = (byte) data.length();

                this.buffer = Unpooled.copiedBuffer(actualData);
            }
        }
    }

    @Getter
    static class PacketPlayInItemName {
        private final @NotNull String name;

        PacketPlayInItemName(@NotNull String name) {
            this.name = name;
        }

    }

    @Getter
    static class ServerboundRenameItemPacket {
        private final @NotNull String name;

        ServerboundRenameItemPacket(@NotNull String name) {
            this.name = name;
        }

    }

}