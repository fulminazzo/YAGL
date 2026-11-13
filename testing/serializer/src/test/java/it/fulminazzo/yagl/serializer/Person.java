package it.fulminazzo.yagl.serializer;

import lombok.Value;
import org.jetbrains.annotations.NotNull;

@Value
class Person {
    @NotNull String name;
    int age;

}
