package it.angrybear.structures;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.function.BiPredicate;

/**
 * A type of set that uses a {@link BiPredicate} to compare two {@link E} elements when calling {@link #add(Object)}.
 *
 * @param <E> the type parameter
 */
public class PredicateSet<E> implements Set<E>, Serializable {
    protected final Set<E> internal;
    /**
     * The Add test.
     */
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
    public boolean isEmpty() {
        return this.internal.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return this.internal.contains(o);
    }

    @Override
    public @NotNull Iterator<E> iterator() {
        return this.internal.iterator();
    }

    @Override
    public Object @NotNull [] toArray() {
        return this.internal.toArray();
    }

    @Override
    public <T> T @NotNull [] toArray(T @NotNull [] ts) {
        return this.internal.toArray(ts);
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
        if (replace) {
            this.internal.add(e);
            return true;
        }
        E internalE = getInternal(e);
        if (internalE == null || this.addTest.test(internalE, e)) {
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

    @Override
    public boolean remove(Object o) {
        return this.internal.remove(o);
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> collection) {
        return this.internal.containsAll(collection);
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends E> collection) {
        boolean added = true;
        for (E e : collection) {
            boolean t = add(e);
            if (added) added = t;
        }
        return added;
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> collection) {
        return this.internal.retainAll(collection);
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> collection) {
        return this.internal.removeAll(collection);
    }

    @Override
    public void clear() {
        this.internal.clear();
    }

    /**
     * Compares the internal set with the given one.
     *
     * @param set the set
     * @return true if they are equal
     */
    public boolean equals(@Nullable Set<?> set) {
        if (set == null) return false;
        return this.internal.equals(set);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Set) return equals((Set<?>) o);
        return super.equals(o);
    }

    @Override
    public String toString() {
        return this.internal.toString();
    }
}
