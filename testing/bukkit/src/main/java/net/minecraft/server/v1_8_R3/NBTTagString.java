package net.minecraft.server.v1_8_R3;

import lombok.Getter;

@Getter
public class NBTTagString extends NBTBase {
    private final String data;

    public NBTTagString(String data) {
        this.data = data;
    }

}
