package it.angrybear.yagl;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

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
}