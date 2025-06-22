package org.bukkit.craftbukkit.v1_8_R3.util;

public class CraftChatMessage {

    public static IChatBaseComponent[] fromString(final String message) {
        return new IChatBaseComponent[]{
                new IChatBaseComponent(message)
        };
    }

    public static class IChatBaseComponent {
        private final String message;

        private IChatBaseComponent(String message) {
            this.message = message;
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof IChatBaseComponent && this.message.equals(((IChatBaseComponent) o).message);
        }

        @Override
        public String toString() {
            return "IChatBaseComponent{" + this.message + "}";
        }

    }

}
