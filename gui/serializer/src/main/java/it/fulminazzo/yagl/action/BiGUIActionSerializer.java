package it.fulminazzo.yagl.action;

import it.fulminazzo.yagl.action.command.BiGUICommand;
import it.fulminazzo.yagl.action.command.BiGUIConsoleCommand;
import it.fulminazzo.yagl.action.message.BiGUIMessage;
import it.fulminazzo.yagl.serializer.SerializableFunctionSerializer;
import it.fulminazzo.yagl.serializer.Serializer;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BiGUIActionSerializer {
    static final String BI_GUI_MESSAGE_TYPE_NAME = "BI_GUI_MESSAGE";
    static final String BI_GUI_COMMAND_TYPE_NAME = "BI_GUI_COMMAND";
    static final String BI_GUI_CONSOLE_COMMAND_TYPE_NAME = "BI_GUI_CONSOLE_COMMAND";

    private static final @NotNull Serializer<BiGUIAction> SERIALIZER = new SerializableFunctionSerializer<>(BiGUIAction.class)
            .setCustomTypeName(BiGUIMessage.class, BI_GUI_MESSAGE_TYPE_NAME)
            .setCustomTypeName(BiGUICommand.class, BI_GUI_COMMAND_TYPE_NAME)
            .setCustomTypeName(BiGUIConsoleCommand.class, BI_GUI_CONSOLE_COMMAND_TYPE_NAME);

    public static @NotNull Serializer<BiGUIAction> getInstance() {
        return SERIALIZER;
    }

}
