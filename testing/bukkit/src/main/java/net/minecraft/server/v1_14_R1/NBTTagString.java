package net.minecraft.server.v1_14_R1;

import lombok.Getter;

@Getter
public class NBTTagString extends NBTBase {
    private final String data;

    public NBTTagString(String data) {
        this.data = data;
    }

}
