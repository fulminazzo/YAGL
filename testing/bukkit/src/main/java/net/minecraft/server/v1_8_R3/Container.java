package net.minecraft.server.v1_8_R3;

import lombok.Getter;

import java.util.Random;

@Getter
public class Container {
    private final int windowId;

    public Container() {
        this(new Random().nextInt());
    }

    public Container(int windowId) {
        this.windowId = windowId;
    }

}
