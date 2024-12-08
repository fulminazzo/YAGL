package it.fulminazzo.yagl.parsers;

import it.fulminazzo.yagl.actions.ActionParsers;
import it.fulminazzo.yagl.contents.requirements.RequirementChecker;
import it.fulminazzo.yagl.guis.ContentsParser;
import it.fulminazzo.yamlparser.configuration.FileConfiguration;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * The type GUI YAGL parser.
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
