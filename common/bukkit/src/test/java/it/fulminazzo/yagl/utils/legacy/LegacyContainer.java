package it.fulminazzo.yagl.utils.legacy;

import lombok.Getter;
import org.bukkit.craftbukkit.v1_14_R1.util.CraftChatMessage;

@Getter
public final class LegacyContainer extends Container {
    private final CraftChatMessage.IChatBaseComponent title;

    public LegacyContainer(String title) {
        this.title = CraftChatMessage.fromString(title)[0];
    }

}
