package net.minecraft.server.v1_14_R1;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public class World {
    private final String name;

    public World(String name) {
        this.name = name;
    }

}
