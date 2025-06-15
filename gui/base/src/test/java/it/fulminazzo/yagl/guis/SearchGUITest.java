package it.fulminazzo.yagl.guis;

import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.yagl.TestUtils;
import it.fulminazzo.yagl.contents.ItemGUIContent;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

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
    
    @Nested
    class SearchFullSizeGUITest {

        @Test
        void testGetQueryCallsSearchGUIGetQuery() {
            SearchGUI<?> gui = mock(SearchGUI.class);

            SearchGUI.SearchFullSizeGUI fullSizeGUI = new SearchGUI.SearchFullSizeGUI();
            fullSizeGUI.setSearchGui(gui);

            fullSizeGUI.getQuery();

            verify(gui).getQuery();
        }

        @Test
        void testSetQueryCallsSearchGUISetQuery() {
            SearchGUI<?> gui = mock(SearchGUI.class);

            SearchGUI.SearchFullSizeGUI fullSizeGUI = new SearchGUI.SearchFullSizeGUI();
            fullSizeGUI.setSearchGui(gui);

            fullSizeGUI.setQuery("any");

            verify(gui).setQuery("any");
        }

        @Test
        void testGetSearchGUIThrowsIfInitialized() {
            SearchGUI.SearchFullSizeGUI fullSizeGUI = new SearchGUI.SearchFullSizeGUI();
            assertThrows(IllegalStateException.class, fullSizeGUI::getSearchGui);
        }

        @Test
        void testCopyMethod() {
            SearchGUI.SearchFullSizeGUI src = new SearchGUI.SearchFullSizeGUI();
            SearchGUI.SearchFullSizeGUI dst = new SearchGUI.SearchFullSizeGUI();
            assertEquals(dst, src);
        }
        
    }

}