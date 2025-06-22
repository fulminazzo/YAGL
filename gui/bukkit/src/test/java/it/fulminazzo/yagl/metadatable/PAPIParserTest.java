package it.fulminazzo.yagl.metadatable;

import org.bukkit.entity.Player;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PAPIParserTest {

    @Test
    void testApply() {
        Player player = mock(Player.class);
        when(player.getName()).thenReturn("fulminazzo");

        MockObject object = new MockObject();
        object = PAPIParser.parse(player, object);

        assertEquals("SHOULD %not% BE CHANGED", MockObject.string1);
        assertNull(object.string2);
        assertEquals("parse fulminazzo", object.string3);
        assertEquals(Arrays.asList("parse", "fulminazzo"),
                object.list);
        assertEquals(new HashMap<String, String>() {{
            put("parse", "fulminazzo");
            put("parsing", "%not-me%");
            put(null, "%player_name%");
            put("%player_name%", null);
        }}, object.map);
    }

    private static class MockObject {
        static String string1 = "SHOULD %not% BE CHANGED";
        String string2 = null;
        String string3 = "parse %player_name%";
        List<String> list = Arrays.asList("parse", "%player_name%");
        Map<String, String> map = new HashMap<String, String>() {{
            put("parse", "%player_name%");
            put("parsing", "%not-me%");
            put(null, "%player_name%");
            put("%player_name%", null);
        }};
    }

}