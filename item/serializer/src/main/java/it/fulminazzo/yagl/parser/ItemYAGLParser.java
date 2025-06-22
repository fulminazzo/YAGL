package it.fulminazzo.yagl.parser;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * The main access point of {@link it.fulminazzo.yagl.item.Item} and related classes parsers.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ItemYAGLParser {

    /**
     * Adds all the module-specific parsers.
     */
    @SuppressWarnings("deprecation")
    public static void addAllParsers() {
        YAGLParser.addAllParsers();
        WrappersYAGLParser.addAllParsers();
    }

}
