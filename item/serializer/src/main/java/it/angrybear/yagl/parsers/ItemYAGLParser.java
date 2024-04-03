package it.angrybear.yagl.parsers;

/**
 * The type Item YAGL parser.
 */
public final class ItemYAGLParser {

    private ItemYAGLParser() {}

    /**
     * Adds all the module specific parsers.
     */
    public static void addAllParsers() {
        YAGLParser.addAllParsers();
        WrappersYAGLParser.addAllParsers();
    }
}
