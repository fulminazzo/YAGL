package it.fulminazzo.yagl.structures;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.AbstractSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.function.BiPredicate;

/**
 * A type of set that uses a {@link BiPredicate} to compare two {@link E} elements when calling {@link #add(Object)}.
 *
 * @param <E> the type parameter
 */
public class PredicateSet<E> extends AbstractSet<E> implements Serializable {
    protected final Set<E> internal;
    protected final BiPredicate<E, E> addTest;

    /**
     * Instantiates a new Predicate set.
     *
     * @param addTest the predicate used to verify if an element is eligible for adding
     */
    public PredicateSet(final @NotNull BiPredicate<E, E> addTest) {
        this.addTest = addTest;
        this.internal = new HashSet<>();
    }

    @Override
    public int size() {
        return this.internal.size();
    }

    @Override
    public @NotNull Iterator<E> iterator() {
        return this.internal.iterator();
    }

    @Override
    public boolean add(E e) {
        return add(e, false);
    }

    /**
     * Adds the given element to the set.
     *
     * @param e       the element
     * @param replace if true, replace nonetheless.
     * @return true if it was added
     */
    public boolean add(E e, boolean replace) {
        E internalE = getInternal(e);
        if (internalE == null || (this.addTest.test(internalE, e) || replace)) {
            if (internalE != null) this.internal.remove(internalE);
            this.internal.add(e);
            return true;
        }
        return false;
    }

    /**
     * Gets the internal element that equals to the given one.
     *
     * @param e the element
     * @return the internal element
     */
    protected @Nullable E getInternal(@Nullable E e) {
        return e == null ? null : this.internal.stream().filter(t -> t.equals(e)).findFirst().orElse(null);
    }
}
