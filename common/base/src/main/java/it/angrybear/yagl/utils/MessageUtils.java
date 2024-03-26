package it.angrybear.yagl.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The type Message utils.
 */
public class MessageUtils {
    private static final String COLOR_CHAR = "§";
    private static final String COLOR_REGEX = "[A-Fa-f0-9]";
    private static final String STYLE_REGEX = "[LlKkRrOoUu]";

    /**
     * Recolors the given string using Minecraft default color codes.
     *
     * @param message the message
     * @return the string
     */
    public static String color(String message) {
        if (message == null) return null;
        Matcher matcher = Pattern.compile("&(" + COLOR_REGEX + "|" + STYLE_REGEX + ")").matcher(message);
        while (matcher.find())
            message = message.replace(matcher.group(), COLOR_CHAR + matcher.group(1));
        return parseHexColors(message);
    }

    private static String parseHexColors(String message) {
        Matcher matcher = Pattern.compile("#(" + COLOR_REGEX + "{6})").matcher(message);
        while (matcher.find()) {
            String match = matcher.group();
            String replacement = COLOR_CHAR + "x";
            replacement += COLOR_CHAR + String.join(COLOR_CHAR, matcher.group(1).split(""));
            message = message.replace(match, replacement);
        }
        return message;
    }

    public static String decolor(String message) {
        return decolor(message, "&");
    }

    public static String decolor(String message, String character) {
        if (message == null) return null;
        Matcher matcher = Pattern.compile(COLOR_CHAR + "(" + COLOR_REGEX + "|" + STYLE_REGEX + ")").matcher(message);
        while (matcher.find())
            message = message.replace(matcher.group(), character + matcher.group(1));
        return message;
    }
}
