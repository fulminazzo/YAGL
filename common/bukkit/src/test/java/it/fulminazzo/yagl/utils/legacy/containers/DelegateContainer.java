package it.fulminazzo.yagl.utils.legacy.containers;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class DelegateContainer extends Container {
    private final Container delegate;
    private String cachedTitle;

    public DelegateContainer(String title) {
        this.delegate = new Container(
                DefaultContainers.GENERIC_9x3,
                new ObsoleteContainer(title)
        );
        this.cachedTitle = title;
    }

}
