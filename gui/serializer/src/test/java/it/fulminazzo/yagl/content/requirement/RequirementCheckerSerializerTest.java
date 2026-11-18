package it.fulminazzo.yagl.content.requirement;

import it.fulminazzo.yagl.serializer.SerializableFunctionSerializer;
import it.fulminazzo.yagl.serializer.SerializerTestHelper;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

public class RequirementCheckerSerializerTest extends SerializerTestHelper<RequirementChecker> {

    protected RequirementCheckerSerializerTest() {
        super(RequirementChecker.class, RequirementCheckerSerializer.getInstance());
    }

    @ParameterizedTest
    @MethodSource("testData")
    void testDeserialize(final @NotNull RequirementChecker expected, final @NotNull Object raw) throws IOException {
        Class<? extends @NotNull RequirementChecker> expectedType = expected.getClass();
        RequirementChecker actual = load(getFileName(expected));
        assertNotNull(actual);
        assertEquals(expected.serialize(), actual.serialize());
        assumeFalse(expectedType.isSynthetic());
        assertEquals(expectedType, actual.getClass());
    }

    @ParameterizedTest
    @MethodSource("testData")
    void testSerialize(final @NotNull RequirementChecker guiAction, final @NotNull Object expected) throws IOException {
        String fileName = getFileName(guiAction);
        if (expected instanceof String) {
            String raw = readRawString(dump(guiAction, fileName));
            assertNotNull(raw, "Content should not have been null for base type");
            assertFalse(raw.isEmpty(), "Content should not have been empty for base type");
        } else {
            Map<?, ?> raw = readRawMap(dump(guiAction, fileName));
            assertEquals(expected, raw);
        }
    }

    private static Object[][] testData() {
        return new Object[][]{
                {((RequirementChecker) (v) -> false), ""},
                {new PermissionRequirement("test.permission"), Map.of(
                        SerializableFunctionSerializer.TYPE_PATH, RequirementCheckerSerializer.PERMISSION_REQUIREMENT_TYPE_NAME,
                        SerializableFunctionSerializer.CONTENT_PATH, "test.permission"
                )},
        };
    }

    private static @NotNull String getFileName(final @NotNull RequirementChecker action) {
        Class<? extends @NotNull RequirementChecker> requirementType = action.getClass();
        String fileName = requirementType.getSimpleName().toLowerCase(Locale.ROOT);
        if (requirementType.isSynthetic()) fileName = "base";
        return fileName;
    }

}
