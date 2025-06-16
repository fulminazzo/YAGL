package it.fulminazzo.yagl.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import it.fulminazzo.jbukkit.BukkitUtils;
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

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AnvilRenameHandlerTest {

    private Logger logger;
    private Player player;
    private ChannelHandlerContext context;

    private String lastRead;

    private AnvilRenameHandler handler;

    @BeforeAll
    static void setUpAll() {
        BukkitUtils.setupServer();
        Server server = Bukkit.getServer();
        BukkitScheduler scheduler = mock(BukkitScheduler.class);
        when(scheduler.runTaskAsynchronously(any(), any(Runnable.class))).thenAnswer(a -> {
            Runnable runnable = a.getArgument(1);
            runnable.run();
            return null;
        });
        when(server.getScheduler()).thenReturn(scheduler);
    }

    @BeforeEach
    void setUp() {
        this.logger = Logger.getLogger(getClass().getSimpleName());
        this.player = mock(Player.class);
        this.context = mock(ChannelHandlerContext.class);

        this.handler = new AnvilRenameHandler(
                this.logger, this.player,
                (p, n) -> this.lastRead = n
        );
    }

    @Test
    void testObsoleteRead() {
        BukkitTestUtils.mockPlugin(p -> {
            try {
                String expected = "Hello, world!";

                Object packet = new PacketPlayInCustomPayload("MC|ItemName", expected);

                this.handler.channelRead(this.context, packet);

                assertEquals(expected, this.lastRead);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    void testObsoleteReadOfOtherPacketDoesNotCallAction() throws Exception {
        Object packet = new PacketPlayInCustomPayload("SomethingElse", "Hello, world!");

        this.handler.channelRead(this.context, packet);

        assertNull(this.lastRead);
    }

    @Test
    void testLegacyRead() {
        BukkitTestUtils.mockPlugin(p -> {
            try {
                String expected = "Hello, world!";

                Object packet = new PacketPlayInItemName(expected);

                this.handler.channelRead(this.context, packet);

                assertEquals(expected, this.lastRead);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    void testRead() {
        BukkitTestUtils.mockPlugin(p -> {
            try {
                String expected = "Hello, world!";

                Object packet = new ServerboundRenameItemPacket(expected);

                this.handler.channelRead(this.context, packet);

                assertEquals(expected, this.lastRead);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    void testExceptionDoesNotThrow() {
        assertDoesNotThrow(() -> this.handler.channelRead(this.context, null));
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