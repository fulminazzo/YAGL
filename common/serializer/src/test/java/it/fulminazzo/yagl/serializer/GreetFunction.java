package it.fulminazzo.yagl.serializer;

import it.fulminazzo.yagl.SerializableFunction;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
interface GreetFunction extends SerializableFunction {

    @NotNull String greet();

}
