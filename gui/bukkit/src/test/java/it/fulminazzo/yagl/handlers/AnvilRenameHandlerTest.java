package it.fulminazzo.yagl.handlers;

import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class AnvilRenameHandlerTest {

    private Logger logger;
    private Player player;
    private ChannelHandlerContext context;

    private String lastRead;

    private AnvilRenameHandler handler;

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
    void testRead() throws Exception {
        String expected = "Hello, world!";

        Object packet = new ServerboundRenameItemPacket(expected);

        this.handler.channelRead(this.context, packet);

        assertEquals(expected, this.lastRead);
    }

    @Getter
    static class ServerboundRenameItemPacket {
        private final @NotNull String name;

        ServerboundRenameItemPacket(@NotNull String name) {
            this.name = name;
        }

    }

}