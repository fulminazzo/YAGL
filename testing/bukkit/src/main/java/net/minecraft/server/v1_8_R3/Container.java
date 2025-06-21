package net.minecraft.server.v1_8_R3;

import lombok.Getter;

import java.util.Random;

@Getter
public class Container {
    private final String type;

    private final int windowId;

    public Container() {
        this("minecraft:chest", new Random().nextInt());
    }

    public Container(String type, int windowId) {
        this.type = type;
        this.windowId = windowId;
    }

}
