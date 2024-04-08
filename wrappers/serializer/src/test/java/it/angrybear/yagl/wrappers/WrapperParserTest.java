package it.angrybear.yagl.wrappers;

import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.fulmicollection.utils.SerializeUtils;
import it.fulminazzo.yamlparser.configuration.ConfigurationSection;
import it.fulminazzo.yamlparser.configuration.FileConfiguration;
import it.fulminazzo.yamlparser.exceptions.YAMLException;
import it.fulminazzo.yamlparser.utils.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WrapperParserTest {

    @Test
    void testSaveAndLoadPotion() throws IOException {
        Wrapper expected = new Potion("strength");
        List<Wrapper> actual = saveAndLoad(expected);
        assertNotNull(actual);
        assertEquals(expected.getClass().getDeclaredConstructors().length, actual.size());
        for (Wrapper w : actual)
            assertEquals(expected, w);
    }

    @Test
    void testSaveAndLoadEnchantment() throws IOException {
        Wrapper expected = new Enchantment("unbreaking");
        List<Wrapper> actual = saveAndLoad(expected);
        assertNotNull(actual);
        assertEquals(expected.getClass().getDeclaredConstructors().length, actual.size());
        for (Wrapper w : actual)
            assertEquals(expected, w);
    }

    @Test
    void testSaveAndLoadSound() throws IOException {
        Wrapper expected = new Sound("villager_happy");
        List<Wrapper> actual = saveAndLoad(expected);
        assertNotNull(actual);
        assertEquals(expected.getClass().getDeclaredConstructors().length, actual.size());
        for (Wrapper w : actual)
            assertEquals(expected, w);
    }

    @Test
    void testSaveAndLoadPotionEffect() throws IOException {
        Wrapper expected = new PotionEffect("darkness");
        List<Wrapper> actual = saveAndLoad(expected);
        assertNotNull(actual);
        assertEquals(expected.getClass().getDeclaredConstructors().length, actual.size());
        for (Wrapper w : actual)
            assertEquals(expected, w);
    }

    @Test
    void testSaveAndLoadInvalidWrapper() {
        Wrapper expected = new MockWrapper();
        assertThrowsExactly(YAMLException.class, () -> saveAndLoad(expected));
    }

    @Test
    void testSaveAndLoadInvalidFieldsWrapper() {
        Wrapper expected = new MockFieldWrapper(new RuntimeException());
        assertThrowsExactly(IllegalArgumentException.class, () -> saveAndLoad(expected));
    }

    @SuppressWarnings({"unchecked"})
    private @Nullable <T extends Wrapper> List<T> saveAndLoad(final @Nullable T t) throws IOException {
        WrapperParser.addAllParsers();
        final Class<T> clazz = (Class<T>) (t == null ? Wrapper.class : t.getClass());
        String name = t == null ? "wrapper" : clazz.getSimpleName().toLowerCase();

        File file = save(t, name, clazz);
        return load(file, name, clazz);
    }

    private static <T extends Wrapper> @Nullable List<T> load(@NotNull File file, @NotNull String name, @NotNull Class<T> clazz) throws IOException {
        if (!file.exists()) {
            FileUtils.createNewFile(file);
            FileConfiguration configuration = new FileConfiguration(file);
            configuration.createSection(name);
            configuration.set("value-class", SerializeUtils.serializeToBase64(clazz.getCanonicalName()));
            configuration.save();
        }
        FileConfiguration configuration = new FileConfiguration(file);
        return configuration.getList(name, clazz);
    }

    private static <T extends Wrapper> @NotNull File save(@Nullable T t, @NotNull String name, @NotNull Class<T> clazz) throws IOException {
        File file = getFile(name);
        if (file.exists()) FileUtils.deleteFile(file);
        FileUtils.createNewFile(file);

        FileConfiguration configuration = new FileConfiguration(file);
        ConfigurationSection section = configuration.createSection(name);

        Refl<?> tRefl = new Refl<>(t);
        @NotNull List<Field> fields = tRefl.getNonStaticFields();
        for (int i = 0; i < fields.size(); i++) {
            StringBuilder builder = new StringBuilder();
            for (int j = 0; j <= i; j++) {
                Object o = tRefl.getFieldObject(fields.get(j));
                if (o != null) builder.append(o).append(":");
            }
            section.set(String.valueOf(i), builder.substring(0, Math.max(0, builder.length() - 1)));
        }

        section.set("value-class", SerializeUtils.serializeToBase64(clazz.getCanonicalName()));
        configuration.save();
        return file;
    }

    private static File getFile(String name) {
        return new File("build/resources/test/" + name + ".yml");
    }

    private static class MockWrapper extends Wrapper {
        private final String name = "mock";

        @Override
        public String getName() {
            return this.name;
        }
    }

    private static class MockFieldWrapper extends Wrapper {
        private final Exception exception;

        public MockFieldWrapper(Exception exception) {
            this.exception = exception;
        }

        @Override
        public String getName() {
            return "mock";
        }
    }
}