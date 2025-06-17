package it.fulminazzo.yagl.utils.current;

import lombok.Getter;

@Getter
public class CraftPlayer<H> {
    private final H handle;

    CraftPlayer(H handle) {
        this.handle = handle;
    }

}
