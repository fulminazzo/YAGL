package net.minecraft.server.v1_14_R1;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
public class NBTTagCompound extends NBTBase {
    private final @NotNull Map<String, NBTBase> data;

    public NBTTagCompound() {
        this.data = new LinkedHashMap<>();
    }

    @Override
    public void set(@NotNull String key, @NotNull NBTBase value) {
        this.data.put(key, value);
    }

}
