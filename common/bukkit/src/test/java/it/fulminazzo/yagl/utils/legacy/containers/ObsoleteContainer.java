package it.fulminazzo.yagl.utils.legacy.containers;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class ObsoleteContainer extends Container {
    private String title;

    public ObsoleteContainer(String title) {
        this.title = title;
    }

}
