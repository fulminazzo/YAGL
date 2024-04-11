package it.angrybear.yagl.guis;

import it.angrybear.yagl.contents.GUIContent;
import it.fulminazzo.fulmicollection.interfaces.functions.BiFunctionException;
import it.fulminazzo.fulmicollection.interfaces.functions.TriConsumer;
import it.fulminazzo.yamlparser.configuration.ConfigurationSection;
import it.fulminazzo.yamlparser.configuration.IConfiguration;
import it.fulminazzo.yamlparser.parsers.YAMLParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ContentsParser extends YAMLParser<GUIImpl.Contents> {

    public ContentsParser() {
        super(GUIImpl.Contents.class);
    }

    @Override
    protected BiFunctionException<@NotNull IConfiguration, @NotNull String, GUIImpl.@Nullable Contents> getLoader() {
        return (c, s) -> {
            ConfigurationSection section = c.getConfigurationSection(s);
            if (section == null) return null;
            List<GUIContent> contents = c.getList(s, GUIContent.class);
            if (contents == null) return null;
            return new GUIImpl.Contents(contents.toArray(new GUIContent[0]));
        };
    }

    @Override
    protected TriConsumer<@NotNull IConfiguration, @NotNull String, GUIImpl.@Nullable Contents> getDumper() {
        return (c, s, g) -> {
            c.set(s, null);
            if (g == null) return;
            c.setList(s, g.getContents());
        };
    }
}
