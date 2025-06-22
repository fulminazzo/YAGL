package net.minecraft.server.v1_14_4_R1;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Container {
    private final Container container;

    private String title;

    private String cachedTitle;

    public Container() {
        this(null);
    }

    public Container(Container container) {
        this.container = container;
    }

}
