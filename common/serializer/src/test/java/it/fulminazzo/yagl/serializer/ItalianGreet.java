package it.fulminazzo.yagl.serializer;

import org.jetbrains.annotations.NotNull;

final class ItalianGreet implements GreetFunction {
    private static final long serialVersionUID = -8637800499232016278L;

    @Override
    public @NotNull String greet() {
        return "Ciao!";
    }

    @Override
    public @NotNull String serialize() {
        throw new UnsupportedOperationException();
    }

}
