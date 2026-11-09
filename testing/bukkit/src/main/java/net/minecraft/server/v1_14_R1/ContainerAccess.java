package net.minecraft.server.v1_14_R1;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

@Getter
@EqualsAndHashCode
@ToString
public class ContainerAccess {
    private final World world;
    private final BlockPosition blockPosition;

    public ContainerAccess(World world, BlockPosition blockPosition) {
        this.world = world;
        this.blockPosition = blockPosition;
    }

    public static @NotNull ContainerAccess at(final World world, final BlockPosition blockPosition) {
        return new ContainerAccess(world, blockPosition);
    }

}
