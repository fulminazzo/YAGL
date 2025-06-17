package it.fulminazzo.yagl.utils.legacy.containers;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.craftbukkit.v1_14_R1.util.CraftChatMessage;

@Getter
@Setter
public final class LegacyContainer extends Container {
    private final CraftChatMessage.IChatBaseComponent title;

    public LegacyContainer(String title) {
        this.title = CraftChatMessage.fromString(title)[0];
    }

}
