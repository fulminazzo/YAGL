package net.minecraft.server.v1_14_R1;

public class ContainerAccess {
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
