package it.angrybear.yagl;

import it.angrybear.yagl.contents.ItemGUIContent;
import it.angrybear.yagl.guis.GUI;
import it.angrybear.yagl.guis.GUIType;
import it.angrybear.yagl.guis.ResizableGUI;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class MetadatableTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "hello world;hello_world",
            "hello\rworld;hello_world",
            "hello\nworld;hello_world",
            "hello\tworld;hello_world",
            "Hello_world;hello_world",
            "hi-friend;hi_friend",
            "HelloFriend;hello_friend"
    })
    void testVariableParser(String raw) {
        final String[] tmp = raw.split(";");
        final String expected = tmp[1];
        final String actual = tmp[0];
        assertEquals(expected, Metadatable.VARIABLE_PARSER.apply(actual));
    }

    @Test
    void testCopyAllNoReplace() {
        Metadatable m1 = new MockMetadatable();
        Metadatable m2 = new MockMetadatable();
        m1.setVariable("hello", "world");
        m1.setVariable("replace", "friend");
        m2.setVariable("replace", "family");

        m2.copyFrom(m1, false);

        assertEquals("world", m2.getVariable("hello"));
        assertEquals("family", m2.getVariable("replace"));
    }

    @Test
    void testCopyAllReplace() {
        Metadatable m1 = new MockMetadatable();
        Metadatable m2 = new MockMetadatable();
        m1.setVariable("hello", "world");
        m1.setVariable("replace", "friend");
        m2.setVariable("replace", "family");

        m2.copyFrom(m1, true);

        assertEquals("world", m2.getVariable("hello"));
        assertEquals("friend", m2.getVariable("replace"));
    }

    @Test
    void testApply() {
        Metadatable metadatable = new MockMetadatable()
                .setVariable("parsing", "not-me")
                .setVariable("variable", "me")
                .setVariable("not", "hello")
                .setVariable("null", null)
                .unsetVariable("parsing");

        MockObject object = new MockObject();
        object = metadatable.apply(object);

        assertEquals("SHOULD %not% BE CHANGED", MockObject.string1);
        assertNull(object.string2);
        assertEquals("parse me", object.string3);
        assertEquals(Arrays.asList("parse", "me"),
                object.list);
        assertEquals(new HashMap<String, String>(){{
            put("parse", "me");
            put("parsing", "%not-me%");
            put(null, "%variable%");
            put("%variable%", null);
        }}, object.map);
    }

    private static Object[][] metadatables() {
        return new Object[][]{
                new Object[]{ItemGUIContent.newInstance(), ItemGUIContent.class},
                new Object[]{GUI.newGUI(9), GUI.class},
                new Object[]{GUI.newResizableGUI(9), ResizableGUI.class},
                new Object[]{GUI.newGUI(GUIType.ANVIL), GUI.class},
        };
    }

    @ParameterizedTest
    @MethodSource("metadatables")
    void testMetadatableInheritorsReturnTypes(Metadatable metadatable, Class<?> expectedReturnType) {
        TestUtils.testReturnType(metadatable, Metadatable.class, expectedReturnType, m -> m.getName().equals("copyFrom"));
    }

    private static class MockMetadatable implements Metadatable {
        private final Map<String, String> map = new HashMap<>();

        @Override
        public @NotNull Map<String, String> variables() {
            return this.map;
        }
    }

    private static class MockObject {
        static String string1 = "SHOULD %not% BE CHANGED";
        String string2 = null;
        String string3 = "parse %variable%";
        List<String> list = Arrays.asList("parse", "%variable%");
        Map<String, String> map = new HashMap<String, String>(){{
            put("parse", "%variable%");
            put("parsing", "%not-me%");
            put(null, "%variable%");
            put("%variable%", null);
        }};
    }

}