package it.fulminazzo.yagl.wrapper;

import it.fulminazzo.yagl.serializer.Node;
import it.fulminazzo.yagl.serializer.SerializationException;
import it.fulminazzo.yagl.serializer.Serializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * A general {@link Serializer} for {@link Wrapper} objects.
 *
 * @param <W> the type of the wrapper
 * @see Wrapper
 * @see Serializer
 */
public abstract class WrapperSerializer<W extends Wrapper> implements Serializer<W> {
    /**
     * A character to separate serialized data.
     */
    static final @NotNull String DATA_SEPARATOR = ":";

    /**
     * Initializes a new instance of the wrapper with the data in the given list.
     *
     * @param name     the name of the wrapper
     * @param dataList the data list
     * @return the wrapper
     * @throws SerializationException if any error occurs during initialization
     */
    protected abstract @NotNull W initialize(final @NotNull String name, final @NotNull List<Object> dataList) throws SerializationException;

    /**
     * Gets an <b>ordered</b> data list of the given wrapper.
     *
     * @param wrapper the wrapper
     * @return the list
     */
    protected abstract @NotNull List<Object> toDataList(final @NotNull W wrapper);

    /**
     * Gets the default data list associated with this wrapper type.
     *
     * @return the list
     */
    protected abstract @NotNull List<Object> getDefaultDataList();

    @Override
    public @Nullable W deserialize(final @NotNull Node node) throws SerializationException {
        String raw = node.getAsOrNull(String.class);
        if (raw == null) return null;
        String[] data = raw.split(DATA_SEPARATOR);
        String name = data[0];
        List<Object> dataList = new ArrayList<>(getDefaultDataList());
        for (int i = 1; i < data.length; i++)
            dataList.set(i - 1, data[i]);
        return initialize(name, dataList);
    }

    @Override
    public @Nullable Object serialize(final @Nullable W value) {
        if (value == null) return null;
        List<Object> dataList = toDataList(value);
        List<Object> defaultDataList = getDefaultDataList();

        int end = dataList.size();
        while (end > 0 && Objects.equals(dataList.get(end - 1), defaultDataList.get(end - 1)))
            end--;

        StringBuilder builder = new StringBuilder(value.getName());
        for (int i = 0; i < end; i++)
            builder.append(DATA_SEPARATOR).append(dataList.get(i));
        return builder.toString();
    }

    /**
     * Attempts to convert a string to an integer.
     *
     * @param raw the object to convert
     * @return the integer value
     * @throws SerializationException if the conversion fails
     */
    protected static int toInt(final @NotNull Object raw) throws SerializationException {
        return castTo(raw, Integer.class, Integer::valueOf);
    }

    /**
     * Attempts to convert a string to a float.
     *
     * @param raw the object to convert
     * @return the float value
     * @throws SerializationException if the conversion fails
     */
    protected static float toFloat(final @NotNull Object raw) throws SerializationException {
        return castTo(raw, Float.class, Float::valueOf);
    }

    /**
     * Attempts to convert a string to a double.
     *
     * @param raw the object to convert
     * @return the double value
     * @throws SerializationException if the conversion fails
     */
    protected static double toDouble(final @NotNull Object raw) throws SerializationException {
        return castTo(raw, Double.class, Double::valueOf);
    }

    /**
     * Attempts to convert a string to a boolean.
     *
     * @param raw the object to convert
     * @return the boolean value
     * @throws SerializationException if the conversion fails
     */
    protected static boolean toBoolean(final @NotNull Object raw) throws SerializationException {
        return castTo(raw, Boolean.class, s -> {
            if (s.equals(Boolean.TRUE.toString())) return Boolean.TRUE;
            else if (s.equals(Boolean.FALSE.toString())) return Boolean.FALSE;
            else throw new IllegalArgumentException();
        });
    }

    private static <T> @NotNull T castTo(final @NotNull Object raw,
                                         final @NotNull Class<T> expectedType,
                                         final @NotNull Function<String, T> conversionFunction) throws SerializationException {
        try {
            return conversionFunction.apply(raw.toString());
        } catch (IllegalArgumentException e) {
            throw new SerializationException("Invalid value: expected %s but got %s", expectedType.getName(), raw);
        }
    }

}
