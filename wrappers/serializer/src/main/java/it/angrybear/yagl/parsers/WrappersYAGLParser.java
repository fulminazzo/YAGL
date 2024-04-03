package it.angrybear.yagl.parsers;

import it.angrybear.yagl.particles.ParticleOptionParser;
import it.angrybear.yagl.particles.ParticleParser;
import it.angrybear.yagl.wrappers.WrapperParser;
import it.fulminazzo.yamlparser.configuration.FileConfiguration;

/**
 * The type Wrappers YAGL parser.
 */
public final class WrappersYAGLParser {

    private WrappersYAGLParser() {}

    /**
     * Adds all the module specific parsers.
     */
    public static void addAllParsers() {
        YAGLParser.addAllParsers();
        FileConfiguration.addParsers(ParticleParser.class.getPackage().getName());
        ParticleOptionParser.addAllParsers();
        WrapperParser.addAllParsers();
    }
}
