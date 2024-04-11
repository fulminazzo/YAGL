package it.angrybear.yagl.contents;

import it.angrybear.yagl.SerializableFunction;
import it.angrybear.yagl.viewers.Viewer;
import org.jetbrains.annotations.NotNull;

/**
 * A function to verify if the given viewer meets all the requirements specified.
 */
@FunctionalInterface
public interface RequirementChecker extends SerializableFunction {

    /**
     * The verification function
     *
     * @param viewer the viewer
     * @return true if they can access the content
     */
    boolean test(final @NotNull Viewer viewer);
}
