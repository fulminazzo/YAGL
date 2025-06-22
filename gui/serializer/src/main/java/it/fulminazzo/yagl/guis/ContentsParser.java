package it.fulminazzo.yagl.guis;

import it.fulminazzo.yagl.contents.GUIContent;
import it.fulminazzo.fulmicollection.interfaces.functions.BiFunctionException;
import it.fulminazzo.fulmicollection.interfaces.functions.TriConsumer;
import it.fulminazzo.yamlparser.configuration.IConfiguration;
import it.fulminazzo.yamlparser.parsers.YAMLParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * A parser to serialize {@link GUIImpl.Contents}.
 */
public class ContentsParser extends YAMLParser<GUIImpl.Contents> {

    /**
     * Instantiates a new Contents parser.
     */
    public ContentsParser() {
        super(GUIImpl.Contents.class);
    }

    @Override
    protected BiFunctionException<@NotNull IConfiguration, @NotNull String, GUIImpl.@Nullable Contents, Exception> getLoader() {
        return (c, s) -> {
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
