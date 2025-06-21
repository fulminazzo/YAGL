package net.minecraft.server.v1_14_R1;

import it.fulminazzo.fulmicollection.objects.FieldEquable;
import lombok.Getter;

@Getter
public class ContainerAccess extends FieldEquable {
    private final World world;
    private final BlockPosition blockPosition;

    public ContainerAccess(World world, BlockPosition blockPosition) {
        this.world = world;
        this.blockPosition = blockPosition;
    }

    public static ContainerAccess at(final World world, final BlockPosition blockPosition) {
        return new ContainerAccess(world, blockPosition);
    }

}
