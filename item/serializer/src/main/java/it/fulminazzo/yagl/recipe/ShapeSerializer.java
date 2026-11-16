package it.fulminazzo.yagl.recipe;

import it.fulminazzo.yagl.item.recipe.ShapedRecipe;
import it.fulminazzo.yagl.serializer.Node;
import it.fulminazzo.yagl.serializer.SerializationException;
import it.fulminazzo.yagl.serializer.Serializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A {@link Serializer} for {@link ShapedRecipe.Shape} objects.
 *
 * @see ShapedRecipe.Shape
 * @see Serializer
 */
public final class ShapeSerializer implements Serializer<ShapedRecipe.Shape> {
    private static final @NotNull Pattern SHAPE_PATTERN = Pattern.compile("^([0-9]+)x([0-9]+)$");

    @Override
    public @Nullable ShapedRecipe.Shape deserialize(final @NotNull Node node) throws SerializationException {
        String raw = node.getAsOrNull(String.class);
        if (raw == null) return null;
        Matcher matcher = SHAPE_PATTERN.matcher(raw);
        if (!matcher.matches())
            throw new SerializationException("Invalid shape format, expected \"<rows>x<columns>\" but got: %s", raw);
        int rows = Integer.parseInt(matcher.group(1));
        int columns = Integer.parseInt(matcher.group(2));
        return new ShapedRecipe.Shape(rows, columns);
    }

    @Override
    public @Nullable Object serialize(final @Nullable ShapedRecipe.Shape value) {
        return value == null ? null : value.getRows() + "x" + value.getColumns();
    }

}
