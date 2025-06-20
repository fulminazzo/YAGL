package net.minecraft.server.v1_14_R1.containers;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_14_R1.Container;

@Getter
@Setter
public final class ObsoleteContainer extends Container {
    private String title;

    public ObsoleteContainer(String title) {
        this.title = title;
    }

}
