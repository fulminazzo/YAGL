package it.fulminazzo.yagl.utils.legacy.containers;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerContainer extends Container {
    private final Container delegate;

    private String cachedTitle;

    public PlayerContainer() {
        this.delegate = new Container(
                DefaultContainers.GENERIC_9x3,
                new ObsoleteContainer("")
        );
    }

}
