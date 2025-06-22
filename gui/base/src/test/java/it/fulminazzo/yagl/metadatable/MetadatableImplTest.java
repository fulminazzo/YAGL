package it.fulminazzo.yagl.metadatable;

import it.fulminazzo.yagl.TestUtils;
import it.fulminazzo.yagl.content.ItemGUIContent;
import it.fulminazzo.yagl.gui.GUI;
import it.fulminazzo.yagl.gui.GUIType;
import it.fulminazzo.yagl.gui.ResizableGUI;
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
