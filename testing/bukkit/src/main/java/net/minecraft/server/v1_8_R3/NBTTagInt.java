package net.minecraft.server.v1_8_R3;

import lombok.Getter;

@Getter
public class NBTTagInt extends NBTBase {
    private final int data;

    public NBTTagInt(int data) {
        this.data = data;
    }

    public int d() {
        return this.data;
    }

}
