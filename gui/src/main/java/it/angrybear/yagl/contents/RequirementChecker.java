package it.angrybear.yagl.contents;

import it.angrybear.yagl.SerializableFunction;
import it.angrybear.yagl.viewers.Viewer;

import java.util.function.Predicate;

@FunctionalInterface
public interface RequirementChecker extends SerializableFunction, Predicate<Viewer> {
}
