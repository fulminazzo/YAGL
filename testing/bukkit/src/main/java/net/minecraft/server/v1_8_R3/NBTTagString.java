package net.minecraft.server.v1_8_R3;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public class NBTTagString extends NBTBase {
    private final String data;

    public NBTTagString(String data) {
        this.data = data;
    }

}
