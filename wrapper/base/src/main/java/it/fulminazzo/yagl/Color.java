package it.fulminazzo.yagl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;

/**
 * Represents a general HEX color.
 * Used by special particle options.
 */
@SuppressWarnings("unused")
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

    private final byte alpha;
    private final byte red;
    private final byte green;
    private final byte blue;

    /**
     * Instantiates a new Color.
     *
     * @param red   the red
     * @param green the green
     * @param blue  the blue
     */
    public Color(int red, int green, int blue) {
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
    public Color(int alpha, int red, int green, int blue) {
        checkRange(alpha);
        this.alpha = (byte) alpha;
        checkRange(red);
        this.red = (byte) red;
        checkRange(green);
        this.green = (byte) green;
        checkRange(blue);
        this.blue = (byte) blue;
    }

    /**
     * Gets alpha.
     *
     * @return the alpha
     */
    public int getAlpha() {
        return this.alpha & MASK;
    }

    /**
     * Gets red.
     *
     * @return the red
     */
    public int getRed() {
        return this.red & MASK;
    }

    /**
     * Gets green.
     *
     * @return the green
     */
    public int getGreen() {
        return this.green & MASK;
    }

    /**
     * Gets blue.
     *
     * @return the blue
     */
    public int getBlue() {
        return this.blue & MASK;
    }

    private void checkRange(int n) {
        if (n < 0 || n > MASK)
            throw new IllegalArgumentException(String.format("'%s' is not contained between 0 and %s",
                    n, MASK));
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
        return String.format("#%02X%02X%02X%02X", this.alpha, this.red, this.green, this.blue).toUpperCase();
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
        LinkedList<Integer> nums = new LinkedList<>();
        while (!argb.isEmpty()) {
            int n = Integer.parseUnsignedInt(argb.substring(0, 2), 16);
            argb = argb.substring(2);
            nums.add(n);
        }
        if (nums.size() < 4) nums.addFirst(MASK);
        return new Color(nums.get(0), nums.get(1), nums.get(2), nums.get(3));
    }

    /**
     * Gets the name from the corresponding static color.
     * If this is not a static color, it returns null.
     *
     * @return the name
     */
    @Override
    public @Nullable String name() {
        try {
            return super.name();
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Gets the corresponding static color
     *
     * @param index the index
     * @return the color
     */
    public static @NotNull Color valueOf(final int index) {
        return valueOf(index, Color.class);
    }

    /**
     * Gets the corresponding static color
     *
     * @param color the color name
     * @return the color
     */
    public static Color valueOf(final @NotNull String color) {
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
    public boolean equals(Object o) {
        if (o instanceof Color) {
            Color color = (Color) o;
            return this.alpha == color.alpha && this.red == color.red &&
                    this.green == color.green && this.blue == color.blue;
        } else return false;
    }

    @Override
    public int hashCode() {
        int hash = Color.class.hashCode();
        hash = hash * 31 + this.alpha;
        hash = hash * 31 + this.red;
        hash = hash * 31 + this.green;
        hash = hash * 31 + this.blue;
        return hash;
    }

    @Override
    public String toString() {
        String name = name();
        if (name != null) return name;
        return super.toString();
    }

}
