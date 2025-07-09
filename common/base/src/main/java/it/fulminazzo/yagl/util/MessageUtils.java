package it.fulminazzo.yagl.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

    /**
     * Recolors the given string using Minecraft default color codes.
     *
     * @param message the message
     * @return the string
     */
    public static String color(@Nullable String message) {
        if (message == null) return null;
        Matcher matcher = Pattern.compile("&(" + COLOR_REGEX + "|" + STYLE_REGEX + ")").matcher(message);
        while (matcher.find())
            message = message.replace(matcher.group(), COLOR_CHAR + matcher.group(1));
        return parseHexColors(message);
    }

    private static @NotNull String parseHexColors(@NotNull String message) {
        Matcher matcher = Pattern.compile("#(" + COLOR_REGEX + "{6})").matcher(message);
        while (matcher.find()) {
            String match = matcher.group();
            String replacement = COLOR_CHAR + "x";
            replacement += COLOR_CHAR + String.join(COLOR_CHAR, matcher.group(1).split(""));
            message = message.replace(match, replacement);
        }
        return message;
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
        Matcher matcher = Pattern.compile(COLOR_CHAR + "(" + COLOR_REGEX + "|" + STYLE_REGEX + ")").matcher(message);
        while (matcher.find())
            message = message.replace(matcher.group(), character + matcher.group(1));
        return message;
    }

    private static @NotNull String unparseHexColors(@NotNull String message) {
        Matcher matcher = Pattern.compile(COLOR_CHAR + "x((?:" + COLOR_CHAR + COLOR_REGEX + "){6})").matcher(message);
        while (matcher.find()) {
            String match = matcher.group();
            String replacement = "#";
            String repl = matcher.group(1).toUpperCase();
            replacement += String.join("", repl.split(COLOR_CHAR));
            message = message.replace(match, replacement);
        }
        return message;
    }

}
