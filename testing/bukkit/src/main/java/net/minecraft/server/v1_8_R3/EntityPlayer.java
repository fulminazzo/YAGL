package net.minecraft.server.v1_8_R3;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

@Getter
@Setter
public class EntityPlayer {
    private final Player player;
    private final Container defaultContainer;

    private Container playerContainer;

    public EntityPlayer() {
        this(null);
    }

    public EntityPlayer(Player player) {
        this.player = player;
        this.defaultContainer = new Container();
        this.playerContainer = new Container();
    }

}
