package it.angrybear.yagl;

import it.fulminazzo.fulmicollection.interfaces.functions.BiFunctionException;
import it.fulminazzo.fulmicollection.interfaces.functions.TriConsumer;
import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.yamlparser.configuration.IConfiguration;
import it.fulminazzo.yamlparser.parsers.YAMLParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * A class to help test a {@link YAMLParser}.
 *
 * @param <T> the type parameter
 */
public abstract class ParserTestHelper<T> {

    /**
     * Tests if {@link YAMLParser#load(IConfiguration, String)} returns null when the object is null.
     *
     * @throws Exception the exception
     */
    @Test
    void testLoadNull() throws Exception {
        assertNull(getLoader().apply(getConfiguration(null), "path"));
    }

    /**
     * Tests if {@link YAMLParser#dump(IConfiguration, String, Object)} returns null when the object is null.
     */
    @Test
    void testSaveNull() {
        assertDoesNotThrow(() -> getDumper()
                .accept(getConfiguration(null), "path", null));
    }

    /**
     * Gets loader from {@link #getYamlParser()}.
     *
     * @return the loader
     */
    protected BiFunctionException<IConfiguration, String, T> getLoader() {
        return getYamlParser().invokeMethod("getLoader");
    }

    /**
     * Gets dumper from {@link #getYamlParser()}.
     *
     * @return the dumper
     */
    protected TriConsumer<IConfiguration, String, T> getDumper() {
        return getYamlParser().invokeMethod("getDumper");
    }

    /**
     * Gets an implementation of {@link #getParser()}.
     *
     * @return the parser
     */
    @SuppressWarnings("unchecked")
    protected Refl<YAMLParser<T>> getYamlParser() {
        return (Refl<YAMLParser<T>>) new Refl<>(getParser(), new Object[0]);
    }

    /**
     * Local implementation of {@link #prepareConfiguration(String)}.
     *
     * @param returnValue the return value
     * @return the configuration
     */
    protected @NotNull IConfiguration getConfiguration(final @Nullable String returnValue) {
        return prepareConfiguration(returnValue);
    }

    /**
     * The {@link YAMLParser} class to test.
     *
     * @return the parser
     */
    protected abstract Class<?> getParser();

    /**
     * Creates mock of {@link IConfiguration} that returns the given value when using {@link IConfiguration#getString(String)}.
     *
     * @param returnValue the return value
     * @return the configuration
     */
    protected static @NotNull IConfiguration prepareConfiguration(final @Nullable String returnValue) {
        IConfiguration configuration = mock(IConfiguration.class);
        when(configuration.getString(any())).thenReturn(returnValue);
        return configuration;
    }
}
