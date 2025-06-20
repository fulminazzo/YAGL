package net.minecraft.server.v1_14_R1.containers;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_14_R1.Container;
import org.bukkit.craftbukkit.v1_14_R1.util.CraftChatMessage;

@Getter
@Setter
public final class LegacyContainer extends Container {
    private final CraftChatMessage.IChatBaseComponent title;

    public LegacyContainer(String title) {
        this.title = CraftChatMessage.fromString(title)[0];
    }

}
