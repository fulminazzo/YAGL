package it.angrybear.structures;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiPredicate;

/**
 * An implementation of {@link PredicateSet} that provides a {@link BiPredicate} to get the internal {@link E} when calling {@link #getInternal(Object)}.
 *
 * @param <E> the type parameter
 */
public class PComparatorSet<E> extends PredicateSet<E> {
    protected final BiPredicate<E, E> internalPredicate;

    /**
     * Instantiates a new PComparatorSet set.
     *
     * @param predicate the predicate
     */
    public PComparatorSet(@NotNull BiPredicate<E, E> predicate, @NotNull BiPredicate<E, E> internalPredicate) {
        super(predicate);
        this.internalPredicate = internalPredicate;
    }

    @Override
    protected @Nullable E getInternal(@Nullable E e) {
        return e == null ? null : this.internal.stream().filter(t -> this.internalPredicate.test(t, e)).findFirst().orElse(null);
    }
}
