package net.minecraft.network.protocol.game;

import it.fulminazzo.yagl.utils.legacy.containers.Containers;
import lombok.Getter;
import net.minecraft.network.protocol.Packet;
import org.bukkit.craftbukkit.v1_14_R1.util.CraftChatMessage;

@Getter
public class PacketPlayOutOpenWindow extends Packet {
    private final int id;
    private final Containers containerType;
    private final CraftChatMessage.IChatBaseComponent title;

    public PacketPlayOutOpenWindow(int id, Containers containerType, CraftChatMessage.IChatBaseComponent title) {
        this.id = id;
        this.containerType = containerType;
        this.title = title;
    }

}
