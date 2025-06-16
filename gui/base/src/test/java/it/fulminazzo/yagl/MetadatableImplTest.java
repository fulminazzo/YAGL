package it.fulminazzo.yagl;

import it.fulminazzo.yagl.contents.ItemGUIContent;
import it.fulminazzo.yagl.guis.GUI;
import it.fulminazzo.yagl.guis.GUIType;
import it.fulminazzo.yagl.guis.ResizableGUI;
import it.fulminazzo.yagl.metadatable.Metadatable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class MetadatableImplTest {

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

}
