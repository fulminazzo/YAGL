package it.angrybear.yagl.parsers;

import it.angrybear.yagl.actions.ActionParsers;
import it.angrybear.yagl.contents.RequirementChecker;
import it.angrybear.yagl.guis.ContentsParser;
import it.fulminazzo.yamlparser.configuration.FileConfiguration;

public class YAGLParser {

    public static void addAllParsers() {
        FileConfiguration.addParsers(YAGLParser.class.getPackage().getName());
        FileConfiguration.addParsers(new SerializableFunctionParser<>(RequirementChecker.class));
        ActionParsers.addParsers();
        FileConfiguration.addParsers(new ContentsParser());
    }
}
