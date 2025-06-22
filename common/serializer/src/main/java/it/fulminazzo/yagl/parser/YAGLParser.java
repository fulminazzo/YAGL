package it.fulminazzo.yagl.parser;

import it.fulminazzo.yamlparser.configuration.FileConfiguration;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * The type YAGL parser.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class YAGLParser {

    /**
     * Adds all the parsers from the {@link it.fulminazzo.yagl.parser} package to {@link FileConfiguration}.
     * @deprecated This will NOT add module-specific parsers. It is recommended to look for the <b>&lt;module&gt;YAGLParser</b> class instead.
     */
    @Deprecated
    public static void addAllParsers() {
        FileConfiguration.addParsers(YAGLParser.class.getPackage().getName());
    }
}
