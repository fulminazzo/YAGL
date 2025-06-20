package net.minecraft.server.v1_14_R1.containers;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_14_R1.Container;

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
