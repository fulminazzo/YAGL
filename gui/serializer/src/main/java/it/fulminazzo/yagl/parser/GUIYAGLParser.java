package it.fulminazzo.yagl.parser;

import it.fulminazzo.yagl.action.ActionParsers;
import it.fulminazzo.yagl.content.requirement.RequirementChecker;
import it.fulminazzo.yagl.gui.ContentsParser;
import it.fulminazzo.yamlparser.configuration.FileConfiguration;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * The main access point of {@link it.fulminazzo.yagl.gui.GUI} and related classes parsers.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class GUIYAGLParser {

    /**
     * Adds all the module-specific parsers.
     */
    public static void addAllParsers() {
        ItemYAGLParser.addAllParsers();
        FileConfiguration.addParsers(new SerializableFunctionParser<>(RequirementChecker.class));
        ActionParsers.addParsers();
        FileConfiguration.addParsers(ContentsParser.class.getPackage().getName());
    }

}
