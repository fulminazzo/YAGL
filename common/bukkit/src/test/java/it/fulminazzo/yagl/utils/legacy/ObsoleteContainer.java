package it.fulminazzo.yagl.utils.legacy;

import lombok.Getter;

/**
 * Mimics the 1.8 Container class.
 */
@Getter
public final class ObsoleteContainer extends Container {
    private final String title;

    public ObsoleteContainer(String title) {
        this.title = title;
    }

}
