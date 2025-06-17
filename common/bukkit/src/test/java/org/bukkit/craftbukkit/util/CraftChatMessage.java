package org.bukkit.craftbukkit.util;

public class CraftChatMessage {

    public static String[] fromString(final String message) {
        return new String[]{
                "IChatBaseComponent{" + message + "}"
        };
    }

}
