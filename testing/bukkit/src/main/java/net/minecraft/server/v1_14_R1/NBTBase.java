package net.minecraft.server.v1_14_R1;

import it.fulminazzo.yagl.exceptions.NotImplemented;
import org.jetbrains.annotations.NotNull;

public abstract class NBTBase {

    public void set(final @NotNull String key,
                    final @NotNull NBTBase value) {
        throw new NotImplemented();
    }

}
