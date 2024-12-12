package it.fulminazzo.yagl;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class SingleInstance {
    private static final Map<Class<? extends SingleInstance>, SingleInstance> INSTANCES_MAP = new LinkedHashMap<>();

    public SingleInstance() {
        initialize();
    }

    public void initialize() {
        if (INSTANCES_MAP.containsKey(getClass()))
            throw new InstanceAlreadyInitializedException(this);
        else INSTANCES_MAP.put(getClass(), this);
    }

    public void terminate() {
        INSTANCES_MAP.remove(getClass());
    }

    @SuppressWarnings("unchecked")
    public static <T extends SingleInstance> @NotNull T getInstance(Class<T> clazz) {
        T instance = (T) INSTANCES_MAP.get(clazz);
        if (instance == null) throw new InstanceNotInitialized(clazz);
        return instance;
    }

}
