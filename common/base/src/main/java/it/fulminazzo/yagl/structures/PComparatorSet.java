package it.fulminazzo.yagl.structures;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiPredicate;

/**
 * An implementation of {@link PredicateSet} that provides a {@link BiPredicate} to get the internal {@link E} when calling {@link #getInternal(Object)}.
 *
 * @param <E> the type parameter
 */
public class PComparatorSet<E> extends PredicateSet<E> {
    protected final BiPredicate<E, E> comparator;

    /**
     * Instantiates a new PComparatorSet set.
     *
     * @param addTest the predicate used to verify if an element is eligible for adding
     * @param comparator the predicate used to compare two elements when getting the internal value
     */
    public PComparatorSet(@NotNull BiPredicate<E, E> addTest, @NotNull BiPredicate<E, E> comparator) {
        super(addTest);
        this.comparator = comparator;
    }

    @Override
    protected @Nullable E getInternal(@Nullable E e) {
        return e == null ? null : this.internal.stream().filter(t -> this.comparator.test(t, e)).findFirst().orElse(null);
    }
}
