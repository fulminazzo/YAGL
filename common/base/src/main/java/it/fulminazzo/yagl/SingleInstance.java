package it.fulminazzo.yagl;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A special abstract type that allows to define objects only one time.
 * Upon initiating it for the first time, no more instances will be allowed (check {@link #initialize()} for more)
 * unless the {@link #terminate()} method is invoked.
 * <br>
 * It is possible to retrieve any instance using {@link #getInstance(Class)}.
 */
public abstract class SingleInstance {
    private static final Map<Class<? extends SingleInstance>, SingleInstance> INSTANCES_MAP = new LinkedHashMap<>();

    /**
     * Checks if the current instance is already present in {@link #INSTANCES_MAP}.
     * If it is, a {@link InstanceAlreadyInitializedException} is thrown with the instance itself as parameter.
     */
    public void initialize() {
        if (INSTANCES_MAP.containsKey(getClass()))
            throw new InstanceAlreadyInitializedException(this);
        else INSTANCES_MAP.put(getClass(), this);
    }

    /**
     * Removes the current instance from {@link #INSTANCES_MAP}.
     * If it is already removed, a {@link InstanceNotInitialized} is thrown.
     */
    public void terminate() {
        if (INSTANCES_MAP.containsKey(getClass())) INSTANCES_MAP.remove(getClass());
        else throw new InstanceNotInitialized(getClass());
    }

    /**
     * Gets the instance corresponding to the type from {@link #INSTANCES_MAP}.
     * If not present, a {@link InstanceNotInitialized} is thrown.
     *
     * @param <T>   the type of the instance
     * @param clazz the class of the instance
     * @return the instance
     */
    @SuppressWarnings("unchecked")
    public static <T extends SingleInstance> @NotNull T getInstance(Class<T> clazz) {
        T instance = (T) INSTANCES_MAP.get(clazz);
        if (instance == null) throw new InstanceNotInitialized(clazz);
        return instance;
    }

}
