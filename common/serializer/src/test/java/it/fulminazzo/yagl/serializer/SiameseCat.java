package it.fulminazzo.yagl.serializer;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
final class SiameseCat extends Cat {
    final @NotNull String master;
    @NotNull String region;

    public SiameseCat(final @NotNull String name, final @NotNull String master) {
        super(name);
        this.master = master;
    }

}
