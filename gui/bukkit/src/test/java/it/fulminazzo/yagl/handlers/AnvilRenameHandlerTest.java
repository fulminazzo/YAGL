package it.fulminazzo.yagl.handlers;

import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class AnvilRenameHandlerTest {

    private Logger logger;
    private Player player;

    private String lastRead;

    private AnvilRenameHandler handler;

    @BeforeEach
    void setUp() {
        this.logger = Logger.getLogger(getClass().getSimpleName());
        this.player = mock(Player.class);

        this.handler = new AnvilRenameHandler(
                this.logger, this.player,
                (p, n) -> this.lastRead = n
        );
    }

}