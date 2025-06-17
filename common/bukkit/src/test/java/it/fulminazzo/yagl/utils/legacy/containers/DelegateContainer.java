package it.fulminazzo.yagl.utils.legacy.containers;

import lombok.Getter;

@Getter
public final class DelegateContainer extends Container {
    private final Container delegate;

    public DelegateContainer(String title) {
        this.delegate = new Container(
                DefaultContainers.GENERIC_9x3,
                new ObsoleteContainer(title)
        );
    }

}
