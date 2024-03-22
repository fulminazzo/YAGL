package it.angrybear.yagl.actions;

import it.angrybear.yagl.parsers.SerializableFunctionParser;
import it.fulminazzo.yamlparser.configuration.FileConfiguration;

public class ActionParsers {

    public static void addParsers() {
        FileConfiguration.addParsers(new SerializableFunctionParser<>(GUIAction.class));
        FileConfiguration.addParsers(new SerializableFunctionParser<>(GUIItemAction.class));
        FileConfiguration.addParsers(new SerializableFunctionParser<>(BiGUIAction.class));
    }
}
