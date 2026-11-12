package it.fulminazzo.yagl.content.requirement;

import it.fulminazzo.yagl.SerializableFunction;
import it.fulminazzo.yagl.viewer.Viewer;
import org.jetbrains.annotations.NotNull;

/**
 * A function to verify if the given viewer meets all the requirements specified.
 */
@FunctionalInterface
public interface RequirementChecker extends SerializableFunction {

    /**
     * Executes the verification function against the given viewer.
     *
     * @param viewer the viewer
     * @return {@code true} if they can access the content
     */
    boolean test(final @NotNull Viewer viewer);

}
