package net.minecraft.server.v1_8_R3;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_14_R1.Container;

@Getter
@Setter
public class EntityPlayer {
    private final Container defaultContainer;

    private Container playerContainer;

    public EntityPlayer() {
        this.defaultContainer = new Container();
        this.playerContainer = new Container();
    }

}
