package it.fulminazzo.yagl.content.requirement;

import it.fulminazzo.yagl.serializer.SerializableFunctionSerializer;
import it.fulminazzo.yagl.serializer.Serializer;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RequirementCheckerSerializer {
    static final String PERMISSION_REQUIREMENT_TYPE_NAME = "PERMISSION";

    private static final @NotNull Serializer<RequirementChecker> SERIALIZER = new SerializableFunctionSerializer<>(RequirementChecker.class)
            .setCustomTypeName(PermissionRequirement.class, PERMISSION_REQUIREMENT_TYPE_NAME);

    public static @NotNull Serializer<RequirementChecker> getInstance() {
        return SERIALIZER;
    }

}
