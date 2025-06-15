package it.fulminazzo.yagl.guis;

import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.yagl.TestUtils;
import it.fulminazzo.yagl.contents.ItemGUIContent;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SearchGUITest {

    @Test
    void testCopy() {
        SearchGUI<String> src = SearchGUI.newGUI(s -> null, (f, s) -> true);
        src.setContents(0, ItemGUIContent.newInstance("stone"));

        SearchGUI<String> dst = src.copy();

        new Refl<>(dst).setFieldObject("searchFunction", new Refl<>(src).getFieldObject("searchFunction"));
        assertEquals(src, dst);
    }

    @Test
    void testCopyAll() {
        SearchGUI<String> src = SearchGUI.newGUI(s -> null, (f, s) -> true);
        src.setContents(0, ItemGUIContent.newInstance("stone"));

        SearchGUI<String> dst = SearchGUI.newGUI(s -> null, (f, s) -> true);

        src.copyAll(dst, true);

        new Refl<>(dst).setFieldObject("searchFunction", new Refl<>(src).getFieldObject("searchFunction"));
        assertEquals(src, dst);
    }

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