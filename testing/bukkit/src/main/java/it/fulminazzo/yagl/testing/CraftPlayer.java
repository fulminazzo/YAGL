package it.fulminazzo.yagl.testing;

import lombok.Getter;

@Getter
public class CraftPlayer<H> {
    private final H handle;

    CraftPlayer(H handle) {
        this.handle = handle;
    }

}
