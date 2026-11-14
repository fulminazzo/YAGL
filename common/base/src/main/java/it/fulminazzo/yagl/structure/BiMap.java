package it.fulminazzo.yagl.structure;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * A bidirectional map that maintains both forward and inverse mappings.
 * Both keys and values must be unique; inserting a duplicate value will
 * remove the existing key that maps to it.
 *
 * @param <K> the type of keys
 * @param <V> the type of values
 */
@RequiredArgsConstructor
public final class BiMap<K, V> extends AbstractMap<K, V> {
    private final @NotNull Map<K, V> map;
    private final @NotNull Map<V, K> inverseMap;

    /**
     * Instantiates a new BiMap with empty maps for keys and values.
     */
    public BiMap() {
        this(new HashMap<>(), new HashMap<>());
    }

    /**
     * Gets the key corresponding to the given value.
     *
     * @param value the value to look up
     * @return the key corresponding to the given value, or {@code null} if no such key exists
     */
    public @Nullable K getByValue(final @NotNull V value) {
        return inverseMap.get(value);
    }

    /**
     * Removes the mapping for the given value, if it exists.
     *
     * @param value the value to remove
     * @return the key that was removed, or {@code null} if no such key exists
     */
    public @Nullable K removeByValue(final @NotNull V value) {
        K key = inverseMap.remove(value);
        if (key != null) map.remove(key);
        return key;
    }

    @Override
    public V put(final @Nullable K key, final @Nullable V value) {
        V previousValue = remove(key);
        K previousKey = inverseMap.remove(value);
        if (previousKey != null) map.remove(previousKey);
        map.put(key, value);
        inverseMap.put(value, key);
        return previousValue;
    }

    @Override
    public V remove(final @Nullable Object key) {
        V previous = map.remove(key);
        if (previous != null) inverseMap.remove(previous);
        return previous;
    }

    @Override
    public V get(Object key) {
        return map.get(key);
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return inverseMap.containsKey(value);
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public void clear() {
        map.clear();
        inverseMap.clear();
    }

    @Override
    public @NotNull Set<Entry<K, V>> entrySet() {
        return new AbstractSet<>() {
            @Override
            public int size() {
                return map.size();
            }

            @Override
            public void clear() {
                BiMap.this.clear();
            }

            @Override
            public @NotNull Iterator<Entry<K, V>> iterator() {
                Iterator<Entry<K, V>> it = map.entrySet().iterator();
                return new Iterator<>() {
                    private V lastValue;

                    @Override
                    public boolean hasNext() {
                        return it.hasNext();
                    }

                    @Override
                    public Entry<K, V> next() {
                        Entry<K, V> e = it.next();
                        lastValue = e.getValue();
                        return new SimpleImmutableEntry<>(e);
                    }

                    @Override
                    public void remove() {
                        it.remove();
                        inverseMap.remove(lastValue);
                    }
                };
            }
        };
    }
}
