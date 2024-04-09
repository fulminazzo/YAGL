package it.angrybear.yagl;

import it.fulminazzo.fulmicollection.interfaces.functions.BiFunctionException;
import it.fulminazzo.fulmicollection.interfaces.functions.TriConsumer;
import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.yamlparser.configuration.IConfiguration;
import it.fulminazzo.yamlparser.parsers.YAMLParser;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class ParserTestHelper {

    @Test
    void testLoadNull() throws Exception {
        assertNull(getLoader().apply(prepareConfiguration(null), "path"));
    }

    @Test
    void testSaveNull() {
        assertDoesNotThrow(() -> getDumper()
                .accept(prepareConfiguration(null), "path", null));
    }

    protected BiFunctionException<IConfiguration, String, Object> getLoader() {
        return getYamlParser().invokeMethod("getLoader");
    }

    protected TriConsumer<IConfiguration, String, Object> getDumper() {
        return getYamlParser().invokeMethod("getLoader");
    }

    protected Refl<YAMLParser<?>> getYamlParser() {
        return new Refl<>(getParser(), new Object[0]);
    }

    protected abstract Class<YAMLParser<?>> getParser();

    protected static @NotNull IConfiguration prepareConfiguration(String returnValue) {
        IConfiguration configuration = mock(IConfiguration.class);
        when(configuration.getString(any())).thenReturn(returnValue);
        return configuration;
    }
}
