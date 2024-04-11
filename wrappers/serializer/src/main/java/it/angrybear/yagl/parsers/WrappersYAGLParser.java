package it.angrybear.yagl.parsers;

import it.angrybear.yagl.particles.ParticleOptionParser;
import it.angrybear.yagl.particles.ParticleParser;
import it.angrybear.yagl.wrappers.WrapperParser;
import it.fulminazzo.yamlparser.configuration.FileConfiguration;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * The type Wrappers YAGL parser.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class WrappersYAGLParser {

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
