package it.fulminazzo.yagl.action;

import it.fulminazzo.yagl.action.command.GUICommand;
import it.fulminazzo.yagl.action.command.GUIConsoleCommand;
import it.fulminazzo.yagl.action.message.GUIMessage;
import it.fulminazzo.yagl.serializer.SerializableFunctionSerializer;
import it.fulminazzo.yagl.serializer.Serializer;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class GUIActionSerializer {
    static final @NotNull String GUI_MESSAGE_TYPE_NAME = "GUI_MESSAGE";
    static final @NotNull String GUI_COMMAND_TYPE_NAME = "GUI_COMMAND";
    static final @NotNull String GUI_CONSOLE_COMMAND_TYPE_NAME = "GUI_CONSOLE_COMMAND";

    private static final @NotNull Serializer<GUIAction> SERIALIZER = new SerializableFunctionSerializer<>(GUIAction.class)
            .setCustomTypeName(GUIMessage.class, GUI_MESSAGE_TYPE_NAME)
            .setCustomTypeName(GUICommand.class, GUI_COMMAND_TYPE_NAME)
            .setCustomTypeName(GUIConsoleCommand.class, GUI_CONSOLE_COMMAND_TYPE_NAME);

    public static @NotNull Serializer<GUIAction> getInstance() {
        return SERIALIZER;
    }

}
