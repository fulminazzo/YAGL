package it.fulminazzo.yagl.parsers;

import it.fulminazzo.yagl.items.recipes.ShapedRecipe;
import it.fulminazzo.fulmicollection.interfaces.functions.BiFunctionException;
import it.fulminazzo.fulmicollection.interfaces.functions.TriConsumer;
import it.fulminazzo.yamlparser.configuration.IConfiguration;
import it.fulminazzo.yamlparser.parsers.YAMLParser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A parser to serialize {@link ShapedRecipe.Shape}.
 */
public final class ShapeParser extends YAMLParser<ShapedRecipe.Shape> {
    static final String FORMAT = "(?<rows>\\d+)x(?<columns>\\d+)";

    /**
     * Instantiates a new Shape parser.
     */
    public ShapeParser() {
        super(ShapedRecipe.Shape.class);
    }

    @Override
    protected BiFunctionException<IConfiguration, String, ShapedRecipe.Shape, Exception> getLoader() {
        return (c, s) -> {
            String converted = c.getString(s);
            if (converted == null) return null;
            final Matcher matcher = Pattern.compile(FORMAT).matcher(converted);
            if (matcher.matches()) {
                final int rows = Integer.parseInt(matcher.group("rows"));
                final int columns = Integer.parseInt(matcher.group("columns"));
                return new ShapedRecipe.Shape(rows, columns);
            } else throw new IllegalArgumentException(String.format("'%s' does not match format '%s'",
                    converted, "<rows>x<columns>"));
        };
    }

    @Override
    protected TriConsumer<IConfiguration, String, ShapedRecipe.Shape> getDumper() {
        return (c, s, sh) -> {
            c.set(s, null);
            if (sh == null) return;
            String converted = FORMAT
                    .replace("(?<rows>\\d+)", String.valueOf(sh.getRows()))
                    .replace("(?<columns>\\d+)", String.valueOf(sh.getColumns()));
            c.set(s, converted);
        };
    }
}
