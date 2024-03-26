package it.angrybear.yagl.parsers;

import it.angrybear.yagl.particles.BlockDataOptionParser;
import it.angrybear.yagl.particles.ParticleOptionParser;
import it.angrybear.yagl.particles.ParticleParser;
import it.angrybear.yagl.wrappers.WrapperParser;
import it.fulminazzo.yamlparser.configuration.FileConfiguration;

/**
 * The type Wrappers YAGL parser.
 */
public class WrappersYAGLParser {

    /**
     * Adds all the module specific parsers.
     */
    public static void addAllParsers() {
        YAGLParser.addAllParsers();
        FileConfiguration.addParsers(new ParticleParser());
        FileConfiguration.addParsers(new BlockDataOptionParser());
        ParticleOptionParser.addAllParsers();
        WrapperParser.addAllParsers();
    }
}
