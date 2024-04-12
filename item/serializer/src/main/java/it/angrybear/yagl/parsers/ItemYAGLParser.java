package it.angrybear.yagl.parsers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * The type Item YAGL parser.
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
