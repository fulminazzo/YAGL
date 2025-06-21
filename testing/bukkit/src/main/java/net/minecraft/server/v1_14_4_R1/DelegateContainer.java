package net.minecraft.server.v1_14_4_R1;

import lombok.Getter;

@Getter
public class DelegateContainer extends Container{
    private final Container delegate;

    public DelegateContainer(Container delegate) {
        this.delegate = delegate;
    }

}
