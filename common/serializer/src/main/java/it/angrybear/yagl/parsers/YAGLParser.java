package it.angrybear.yagl.parsers;

import it.fulminazzo.yamlparser.configuration.FileConfiguration;

/**
 * The type YAGL parser.
 */
public final class YAGLParser {

    private YAGLParser() {}

    /**
     * Adds all the parsers from the {@link it.angrybear.yagl.parsers} package to {@link FileConfiguration}.
     * @deprecated This will NOT add module specific parsers. It is recommended to look for the <b>&lt;module&gt;YAGLParser</b> class instead.
     */
    @Deprecated
    public static void addAllParsers() {
        FileConfiguration.addParsers(YAGLParser.class.getPackage().getName());
    }
}
