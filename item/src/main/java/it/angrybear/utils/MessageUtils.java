package it.angrybear.utils;

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
        return message;
    }
}
