package it.angrybear.yagl.parsers;

import it.angrybear.yagl.actions.ActionParsers;
import it.angrybear.yagl.contents.RequirementChecker;
import it.angrybear.yagl.guis.ContentsParser;
import it.fulminazzo.yamlparser.configuration.FileConfiguration;

public class GUIYAGLParser {

    public static void addAllParsers() {
        ItemYAGLParser.addAllParsers();
        //TODO: rework
        FileConfiguration.addParsers(new SerializableFunctionParser<>(RequirementChecker.class));
        ActionParsers.addParsers();
        FileConfiguration.addParsers(new ContentsParser());
    }
}
