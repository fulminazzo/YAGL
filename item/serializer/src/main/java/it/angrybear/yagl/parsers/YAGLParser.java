package it.angrybear.yagl.parsers;

import it.fulminazzo.yamlparser.configuration.FileConfiguration;

public class YAGLParser {

    public static void addAllParsers() {
        FileConfiguration.addParsers(YAGLParser.class.getPackage().getName());
    }
}
