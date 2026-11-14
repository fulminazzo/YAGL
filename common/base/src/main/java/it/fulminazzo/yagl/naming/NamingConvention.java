package it.fulminazzo.yagl.naming;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Represents a naming convention.
 * <br>
 * All the classes in this package have been imported from the blocksmith project
 * <a href="https://github.com/fulminazzo/blocksmith">Click here</a> for more.
 */
interface NamingConvention {

    /**
     * Splits the string into tokens according to the convention rules.
     *
     * @param input the input
     * @return the tokens
     */
    @NotNull List<String> tokenize(final @NotNull String input);

    /**
     * Converts the tokens to a string in the current convention.
     *
     * @param tokens the tokens
     * @return the string
     */
    @NotNull String format(final @NotNull List<String> tokens);

}
