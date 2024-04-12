package it.angrybear.yagl.actions;

import it.angrybear.yagl.parsers.SerializableFunctionParser;
import it.fulminazzo.yamlparser.configuration.FileConfiguration;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class ActionParsers {

    public static void addParsers() {
        FileConfiguration.addParsers(new SerializableFunctionParser<>(GUIAction.class));
        FileConfiguration.addParsers(new SerializableFunctionParser<>(GUIItemAction.class));
        FileConfiguration.addParsers(new SerializableFunctionParser<>(BiGUIAction.class));
    }
}
