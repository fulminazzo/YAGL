package it.fulminazzo.yagl.particle;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public final class ValueMap {
    private final @NotNull Map<String, Object> values;

    @SuppressWarnings("unchecked")
    public <T> @NotNull T get(final @NotNull String name) {
        return (T) values.get(name);
    }

}
