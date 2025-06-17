package net.minecraft.server.v1_8_R3;

import lombok.Getter;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftChatMessage;

@Getter
public class PacketPlayOutOpenWindow extends Packet {
    private final int id;
    private final String containerType;
    private final CraftChatMessage.IChatBaseComponent title;
    private final int size;

    public PacketPlayOutOpenWindow(int id, String containerType, CraftChatMessage.IChatBaseComponent title, int size) {
        this.id = id;
        this.containerType = containerType;
        this.title = title;
        this.size = size;
    }

}
