package it.angrybear.yagl.actions;

import it.angrybear.yagl.parsers.SerializableFunctionParser;
import it.fulminazzo.yamlparser.configuration.FileConfiguration;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * The type Action parsers.
 */
@NoArgsConstructor(access = AccessLevel.NONE)
public final class ActionParsers {

    /**
     * Adds all parsers required to serialize {@link it.angrybear.yagl.SerializableFunction} implementations.
     */
    public static void addParsers() {
        FileConfiguration.addParsers(new SerializableFunctionParser<>(GUIAction.class));
        FileConfiguration.addParsers(new SerializableFunctionParser<>(GUIItemAction.class));
        FileConfiguration.addParsers(new SerializableFunctionParser<>(BiGUIAction.class));
    }
}
