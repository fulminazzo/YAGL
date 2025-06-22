package it.fulminazzo.yagl.actions;

import it.fulminazzo.yagl.SerializableFunction;
import it.fulminazzo.yagl.parsers.SerializableFunctionParser;
import it.fulminazzo.yamlparser.configuration.FileConfiguration;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * The type Action parsers.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ActionParsers {

    /**
     * Adds all parsers required to serialize {@link SerializableFunction} implementations.
     */
    public static void addParsers() {
        FileConfiguration.addParsers(new SerializableFunctionParser<>(GUIAction.class));
        FileConfiguration.addParsers(new SerializableFunctionParser<>(GUIItemAction.class));
        FileConfiguration.addParsers(new SerializableFunctionParser<>(BiGUIAction.class));
    }
}
