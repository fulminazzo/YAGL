package it.fulminazzo.yagl;

import it.fulminazzo.yagl.util.ValidationUtils;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.function.Function;

/**
 * Represents a general HEX color.
 * Used by special particle options.
 */
@SuppressWarnings("unused")
@EqualsAndHashCode(callSuper = false)
public final class Color extends ClassEnum {
    public static final Color WHITE = fromARGB("#FFFFFFFF");
    public static final Color SILVER = fromARGB("#FFC0C0C0");
    public static final Color GRAY = fromARGB("#FF808080");
    public static final Color BLACK = fromARGB("#FF000000");
    public static final Color RED = fromARGB("#FFFF0000");
    public static final Color MAROON = fromARGB("#FF800000");
    public static final Color YELLOW = fromARGB("#FFFFFF00");
    public static final Color OLIVE = fromARGB("#FF808000");
    public static final Color LIME = fromARGB("#FF00FF00");
    public static final Color GREEN = fromARGB("#FF008000");
    public static final Color AQUA = fromARGB("#FF00FFFF");
    public static final Color TEAL = fromARGB("#FF008080");
    public static final Color BLUE = fromARGB("#FF0000FF");
    public static final Color NAVY = fromARGB("#FF000080");
    public static final Color FUCHSIA = fromARGB("#FFFF00FF");
    public static final Color PURPLE = fromARGB("#FF800080");
    public static final Color ORANGE = fromARGB("#FFFFA500");
    private static final int MASK = 255;

    private final @Range(from = 0, to = MASK) byte alpha;
    private final @Range(from = 0, to = MASK) byte red;
    private final @Range(from = 0, to = MASK) byte green;
    private final @Range(from = 0, to = MASK) byte blue;

    /**
     * Instantiates a new Color.
     *
     * @param red   the red
     * @param green the green
     * @param blue  the blue
     */
    public Color(final @Range(from = 0, to = MASK) int red,
                 final @Range(from = 0, to = MASK) int green,
                 final @Range(from = 0, to = MASK) int blue) {
        this(MASK, red, green, blue);
    }

    /**
     * Instantiates a new Color.
     *
     * @param alpha the alpha
     * @param red   the red
     * @param green the green
     * @param blue  the blue
     */
    public Color(final @Range(from = 0, to = MASK) int alpha,
                 final @Range(from = 0, to = MASK) int red,
                 final @Range(from = 0, to = MASK) int green,
                 final @Range(from = 0, to = MASK) int blue) {
        ValidationUtils.checkRange("alpha", alpha, 0, MASK);
        this.alpha = (byte) alpha;
        ValidationUtils.checkRange("red", red, 0, MASK);
        this.red = (byte) red;
        ValidationUtils.checkRange("green", green, 0, MASK);
        this.green = (byte) green;
        ValidationUtils.checkRange("blue", blue, 0, MASK);
        this.blue = (byte) blue;
    }

    /**
     * Gets the alpha value of the color.
     *
     * @return the alpha value
     */
    public @Range(from = 0, to = MASK) int getAlpha() {
        return this.alpha & MASK;
    }

    /**
     * Gets the red value of the color.
     *
     * @return the red value
     */
    public @Range(from = 0, to = MASK) int getRed() {
        return this.red & MASK;
    }

    /**
     * Gets the green value of the color.
     *
     * @return the green value
     */
    public @Range(from = 0, to = MASK) int getGreen() {
        return this.green & MASK;
    }

    /**
     * Gets the blue value of the color.
     *
     * @return the blue value
     */
    public @Range(from = 0, to = MASK) int getBlue() {
        return this.blue & MASK;
    }

    /**
     * Converts this color to an RGB string.
     *
     * @return the string
     */
    public @NotNull String toRGB() {
        return "#" + toARGB().substring(3);
    }

    /**
     * Converts this color to an ARGB string.
     *
     * @return the string
     */
    public @NotNull String toARGB() {
        return String.format("#%02X%02X%02X%02X", this.alpha, this.red, this.green, this.blue);
    }

    /**
     * Converts the given ARGB string to a {@link Color}.
     * An RGB string is also accepted (the alpha value will be set to {@link #MASK}).
     *
     * @param argb the string
     * @return the color
     */
    public static @NotNull Color fromARGB(@NotNull String argb) {
        if (argb.startsWith("#")) argb = argb.substring(1);
        if (argb.length() != 8 && argb.length() != 6)
            throw new IllegalArgumentException(String.format("Invalid ARGB string provided '%s'", argb));

        final Function<String, Integer> convertHex = s -> Integer.parseUnsignedInt(s.substring(0, 2), 16);
        int first = convertHex.apply(argb);
        argb = argb.substring(2);
        int second = convertHex.apply(argb);
        argb = argb.substring(2);
        int third = convertHex.apply(argb);
        argb = argb.substring(2);
        if (argb.isEmpty())
            return new Color(MASK, first, second, third);
        else
            return new Color(first, second, third, convertHex.apply(argb));
    }

    /**
     * Returns the index of the current color.
     * If the color is custom, the index will be {@code -1}.
     *
     * @return the index
     */
    @Override
    public @Range(from = -1, to = Integer.MAX_VALUE) int ordinal() {
        return super.ordinal();
    }

    /**
     * Gets the name from the corresponding static color.
     * If this is not a static color, it returns {@code null}.
     *
     * @return the name
     */
    @Override
    public @Nullable String name() {
        return isKnownEnum() ? super.name() : null;
    }

    /**
     * Gets the static color at the given index.
     *
     * @param index the index
     * @return the color
     */
    public static @NotNull Color valueOf(final int index) {
        return valueOf(index, Color.class);
    }

    /**
     * Gets the static color with the given name.
     *
     * @param color the color name
     * @return the color
     */
    public static @NotNull Color valueOf(final @NotNull String color) {
        return valueOf(color, Color.class);
    }

    /**
     * Gets all the static colors.
     *
     * @return the colors
     */
    public static Color @NotNull [] values() {
        return values(Color.class);
    }

    @Override
    public @NotNull String toString() {
        return isKnownEnum() ? super.name() : String.format("%s{%s}", getClass().getSimpleName(), toARGB());
    }

}
