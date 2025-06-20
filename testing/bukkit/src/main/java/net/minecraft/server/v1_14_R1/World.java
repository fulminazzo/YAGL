package net.minecraft.server.v1_14_R1;

import it.fulminazzo.fulmicollection.objects.FieldEquable;
import lombok.Getter;

@Getter
public class World extends FieldEquable {
    private final String name;

    public World(String name) {
        this.name = name;
    }

}
