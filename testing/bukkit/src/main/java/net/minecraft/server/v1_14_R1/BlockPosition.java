package net.minecraft.server.v1_14_R1;

import it.fulminazzo.fulmicollection.objects.FieldEquable;
import lombok.Getter;

@Getter
public class BlockPosition extends FieldEquable {
    private final int x;
    private final int y;
    private final int z;

    public BlockPosition(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

}
