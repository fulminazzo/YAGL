package it.fulminazzo.yagl.parser;

import it.fulminazzo.yagl.particle.ParticleOptionParser;
import it.fulminazzo.yagl.particle.ParticleParser;
import it.fulminazzo.yagl.wrapper.WrapperParser;
import it.fulminazzo.yamlparser.configuration.FileConfiguration;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * The type Wrappers YAGL parser.
 */
@SuppressWarnings("deprecation")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class WrappersYAGLParser {

    /**
     * Adds all the module-specific parsers.
     */
    public static void addAllParsers() {
        YAGLParser.addAllParsers();
        FileConfiguration.addParsers(ParticleParser.class.getPackage().getName());
        ParticleOptionParser.addAllParsers();
        WrapperParser.addAllParsers();
    }
}
