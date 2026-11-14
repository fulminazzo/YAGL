package it.fulminazzo.yagl.serializer;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
final class CustomGreet implements GreetFunction {
    private static final long serialVersionUID = -4395476283107824692L;

    private final @NotNull String greeting;

    @Override
    public @NotNull String greet() {
        return greeting;
    }

    @Override
    public @NotNull String serialize() {
        return greeting;
    }

}
