package it.angrybear.yagl.contents;

import it.angrybear.yagl.SerializableFunction;
import it.angrybear.yagl.viewers.Viewer;

@FunctionalInterface
public interface RequirementChecker extends SerializableFunction {

    boolean test(Viewer viewer);
}
