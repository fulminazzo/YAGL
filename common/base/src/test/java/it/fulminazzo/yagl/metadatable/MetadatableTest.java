package it.fulminazzo.yagl.metadatable;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("serial")
class MetadatableTest {

    @Test
    void testBasicApply() {
        String string = "<variable>";
        Metadatable metadatable = new MockMetadatable()
                .setVariable("variable", "Hello!");
        assertEquals("Hello!", metadatable.apply(string));
    }

    @Test
    void testMultipleApplies() {
        Metadatable metadatable = new MockMetadatable()
                .setVariable("variable", "Alexander");
        String string = "<variable> <variable>";
        string = metadatable.apply(string);
        assertEquals("Alexander Alexander", string);
    }

    @Test
    void testRecursiveApplies() {
        Metadatable metadatable = new MockMetadatable()
                .setVariable("variable1", "<variable2>")
                .setVariable("variable2", "Alexander");
        String string = "<variable1> <variable2>";
        string = metadatable.apply(string);
        assertEquals("Alexander Alexander", string);
    }

    @Test
    void testApplyStackOverflow() {
        Metadatable metadatable = new MockMetadatable()
                .setVariable("first", "hello")
                .setVariable("second", "world");

        First first = new First();

        metadatable.apply(first);

        assertEquals("hello", first.getFirstName());
        assertEquals("world", first.getSecond().getSecondName());
    }

    @Test
    void testEscapedApply() {
        String tmp = "<apply> \\<do_not> <do> me";
        Metadatable metadatable = new MockMetadatable()
                .setVariable("apply", "hello")
                .setVariable("do_not", "BROKEN")
                .setVariable("do", "world");
        String expected = "hello \\<do_not> world me";
        assertEquals(expected, metadatable.apply(tmp));
    }

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

        m2.copyVariablesFrom(m1, false);

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

        m2.copyVariablesFrom(m1, true);

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

        assertEquals("SHOULD <not> BE CHANGED", MockObject.string1);
        assertNull(object.string2);
        assertEquals("parse me", object.string3);

        // List
        List<String> expectedList = Arrays.asList("parse", "me");
        assertEquals(expectedList, object.list);
        assertEquals(List.of("parse", "<variable>"), object.unmodifiableList);
        assertEquals(expectedList, object.anonymousList);

        // Set
        Set<String> expectedSet = new HashSet<>(expectedList);
        assertEquals(expectedSet, object.set);
        assertEquals(Set.of("parse", "<variable>"), object.unmodifiableSet);
        assertEquals(expectedSet, object.anonymousSet);

        // Map
        HashMap<String, String> expectedMap = new HashMap<>() {{
            put("parse", "me");
            put("parsing", "<not-me>");
            put(null, "<variable>");
            put("<variable>", null);
        }};
        assertEquals(expectedMap, object.map);
        assertEquals(Map.of("parse", "<variable>", "parsing", "<not-me>"), object.unmodifiableMap);
        assertEquals(expectedMap, object.anonymousMap);

        assertEquals(Arrays.asList("parse", "<variable>"), object.arrayList);
    }

    private static class MockMetadatable implements Metadatable {
        private final Map<String, String> map = new HashMap<>();

        @Override
        public @NotNull Map<String, String> variables() {
            return this.map;
        }

    }

    private static class MockObject {
        static String string1 = "SHOULD <not> BE CHANGED";
        String string2 = null;
        String string3 = "parse <variable>";

        List<String> list = new ArrayList<>(Arrays.asList("parse", "<variable>"));
        List<String> unmodifiableList = List.of("parse", "<variable>");
        List<String> anonymousList = new ArrayList<>() {{
            add("parse");
            add("<variable>");
        }};

        Set<String> set = new HashSet<>(Arrays.asList("parse", "<variable>"));
        Set<String> unmodifiableSet = Set.of("parse", "<variable>");
        Set<String> anonymousSet = new HashSet<>() {{
            add("parse");
            add("<variable>");
        }};
        
        Map<String, String> map = new HashMap<>();
        Map<String, String> unmodifiableMap = Map.of(
                "parse", "<variable>", 
                "parsing", "<not-me>"
        );
        Map<String, String> anonymousMap = new HashMap<>() {{
            put("parse", "<variable>");
            put("parsing", "<not-me>");
            put(null, "<variable>");
            put("<variable>", null);
        }};
        
        List<String> arrayList = Arrays.asList("parse", "<variable>");
        
        public MockObject() {
            map.put("parse", "<variable>");
            map.put("parsing", "<not-me>");
            map.put(null, "<variable>");
            map.put("<variable>", null);
        }

    }

    @Getter
    private static class First {
        private final Second second;
        private final String firstName;

        public First() {
            this.firstName = "<first>";
            this.second = new Second(this);
        }

    }

    @Getter
    private static class Second {
        private final First first;
        private final String secondName;

        private Second(First first) {
            this.secondName = "<second>";
            this.first = first;
        }

    }

}
