package it.fulminazzo.yagl.item;

import it.fulminazzo.yagl.TestUtils;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class CustomItemTest {

    private static TestCustomItem[] customItems() {
        return new TestCustomItem[]{
                new TestCustomItem(),
                new TestCustomItem("stone"),
                new TestCustomItem("stone", 3)
        };
    }

    @ParameterizedTest
    @MethodSource("customItems")
    void testReturnTypesCustomItem(TestCustomItem customItem) {
        TestUtils.testReturnType(customItem, Item.class, m -> m.getName().equals("copy"));
    }

    public static class TestCustomItem extends CustomItem<TestCustomItem> {

        public TestCustomItem() {
        }

        public TestCustomItem(String material) {
            super(material);
        }

        public TestCustomItem(@Nullable String material, int amount) {
            super(material, amount);
        }

    }

}