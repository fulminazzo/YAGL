package org.bukkit.craftbukkit.v1_14_R1.util;

public class CraftChatMessage {

    public static String[] fromString(final String message) {
        return new String[]{
                "IChatBaseComponent{" + message + "}"
        };
    }

}
