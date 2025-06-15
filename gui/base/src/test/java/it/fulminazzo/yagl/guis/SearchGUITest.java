package it.fulminazzo.yagl.guis;

import it.fulminazzo.yagl.TestUtils;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

class SearchGUITest {

    @Test
    void testReturnTypes() {
        TestUtils.testReturnType(SearchGUI.newGUI(27,
                        c -> null,
                        (t, s) -> false),
                DataGUI.class,
                m -> {
                    for (String s : Arrays.asList("copy", "setPages", "getPage", "copyAll"))
                        if (s.equals(m.getName())) return true;
                    return false;
                });
    }

}