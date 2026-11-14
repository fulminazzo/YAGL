package it.fulminazzo.yagl.serializer;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
abstract class Cat {
    final @NotNull String name;
    int age;

}
