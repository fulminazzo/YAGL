package net.minecraft.server.v1_14_R1;

import it.fulminazzo.fulmicollection.objects.FieldEquable;
import it.fulminazzo.yagl.exception.NotImplementedException;
import org.jetbrains.annotations.NotNull;

public abstract class NBTBase extends FieldEquable {

    public void set(final @NotNull String key,
                    final @NotNull NBTBase value) {
        throw new NotImplementedException();
    }

}
