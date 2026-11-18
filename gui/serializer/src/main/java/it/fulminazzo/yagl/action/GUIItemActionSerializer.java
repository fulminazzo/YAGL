package it.fulminazzo.yagl.action;

import it.fulminazzo.yagl.action.command.GUIItemCommand;
import it.fulminazzo.yagl.action.command.GUIItemConsoleCommand;
import it.fulminazzo.yagl.action.message.GUIItemMessage;
import it.fulminazzo.yagl.serializer.SerializableFunctionSerializer;
import it.fulminazzo.yagl.serializer.Serializer;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class GUIItemActionSerializer {
    static final @NotNull String GUI_MESSAGE_TYPE_NAME = "GUI_ITEM_MESSAGE";
    static final @NotNull String GUI_COMMAND_TYPE_NAME = "GUI_ITEM_COMMAND";
    static final @NotNull String GUI_CONSOLE_COMMAND_TYPE_NAME = "GUI_ITEM_CONSOLE_COMMAND";
    static final @NotNull String GUI_ITEM_CLOSE_TYPE_NAME = "GUI_ITEM_CLOSE";
    static final @NotNull String GUI_ITEM_BACK_TYPE_NAME = "GUI_ITEM_BACK";

    private static final @NotNull Serializer<GUIItemAction> SERIALIZER = new SerializableFunctionSerializer<>(GUIItemAction.class)
            .setCustomTypeName(GUIItemMessage.class, GUI_MESSAGE_TYPE_NAME)
            .setCustomTypeName(GUIItemCommand.class, GUI_COMMAND_TYPE_NAME)
            .setCustomTypeName(GUIItemConsoleCommand.class, GUI_CONSOLE_COMMAND_TYPE_NAME)
            .setCustomTypeName(GUIItemClose.class, GUI_ITEM_CLOSE_TYPE_NAME)
            .setCustomTypeName(GUIItemBack.class, GUI_ITEM_BACK_TYPE_NAME);

    public static @NotNull Serializer<GUIItemAction> getInstance() {
        return SERIALIZER;
    }

}
