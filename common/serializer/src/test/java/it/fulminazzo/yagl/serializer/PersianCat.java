package it.fulminazzo.yagl.serializer;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
final class PersianCat extends Cat {
    @NotNull String color;

    public PersianCat(final @NotNull String name) {
        super(name);
    }

}
