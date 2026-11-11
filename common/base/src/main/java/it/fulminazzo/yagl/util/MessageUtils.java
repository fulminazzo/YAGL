package it.fulminazzo.yagl.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A collection of utilities to work with Minecraft colors.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MessageUtils {
    private static final String COLOR_CHAR = "§";
    private static final String COLOR_REGEX = "[A-Fa-f0-9]";
    private static final String STYLE_REGEX = "[LlKkRrOoUu]";

    private static final @NotNull Pattern HUMAN_COLOR_STYLE_REGEX = Pattern.compile("&(" + COLOR_REGEX + "|" + STYLE_REGEX + ")");
    private static final @NotNull Pattern GAME_COLOR_STYLE_REGEX = Pattern.compile(COLOR_CHAR + "(" + COLOR_REGEX + "|" + STYLE_REGEX + ")");
    private static final @NotNull Pattern HUMAN_HEX_REGEX = Pattern.compile("#(" + COLOR_REGEX + "{6})");
    private static final @NotNull Pattern GAME_HEX_REGEX = Pattern.compile(COLOR_CHAR + "x((?:" + COLOR_CHAR + COLOR_REGEX + "){6})");

    /**
     * Recolors the given string using Minecraft default color codes.
     *
     * @param message the message
     * @return the string
     */
    public static String color(@Nullable String message) {
        if (message == null) return null;
        Matcher matcher = HUMAN_COLOR_STYLE_REGEX.matcher(message);
        StringBuilder builder = new StringBuilder();
        while (matcher.find())
            matcher.appendReplacement(builder, COLOR_CHAR + matcher.group(1));
        matcher.appendTail(builder);
        return parseHexColors(builder.toString());
    }

    private static @NotNull String parseHexColors(@NotNull String message) {
        Matcher matcher = HUMAN_HEX_REGEX.matcher(message);
        StringBuilder builder = new StringBuilder();
        while (matcher.find()) {
            String hex = matcher.group(1);
            String replacement = COLOR_CHAR + "x" + COLOR_CHAR + String.join(COLOR_CHAR, hex.split(""));
            matcher.appendReplacement(builder, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(builder);
        return builder.toString();
    }

    /**
     * "Decolors" the given string by replacing all valid colors characters with the format <i>&amp;&lt;color&gt;</i>.
     *
     * @param message the message
     * @return the string
     */
    public static String decolor(String message) {
        return decolor(message, "&");
    }

    /**
     * "Decolors" the given string by replacing all valid colors characters with the format <i>&lt;character&gt;&lt;color&gt;</i>.
     *
     * @param message   the message
     * @param character the character
     * @return the string
     */
    public static String decolor(@Nullable String message, String character) {
        if (message == null) return null;
        message = unparseHexColors(message);
        Matcher matcher = GAME_COLOR_STYLE_REGEX.matcher(message);
        StringBuilder builder = new StringBuilder();
        while (matcher.find())
            matcher.appendReplacement(builder, character + matcher.group(1));
        matcher.appendTail(builder);
        return builder.toString();
    }

    private static @NotNull String unparseHexColors(@NotNull String message) {
        Matcher matcher = GAME_HEX_REGEX.matcher(message);
        StringBuilder builder = new StringBuilder();
        while (matcher.find()) {
            String repl = matcher.group(1).toUpperCase(Locale.ROOT);
            String replacement = "#" + String.join("", repl.split(COLOR_CHAR));
            matcher.appendReplacement(builder, replacement);
        }
        matcher.appendTail(builder);
        return builder.toString();
    }

}
