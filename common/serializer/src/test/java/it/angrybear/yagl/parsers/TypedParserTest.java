package it.angrybear.yagl.parsers;

import it.fulminazzo.yamlparser.configuration.ConfigurationSection;
import it.fulminazzo.yamlparser.configuration.IConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TypedParserTest {
    private MockClassParser parser;
    private IConfiguration configuration;

    @BeforeEach
    void setUp() {
        this.parser = new MockClassParser();
        this.configuration = mock(IConfiguration.class);
        ConfigurationSection section = new ConfigurationSection(this.configuration, "name");
        when(this.configuration.getConfigurationSection(anyString())).thenReturn(section);
        when(this.configuration.getString(anyString())).thenReturn(null);
    }

    @Test
    void testLoadWithNoType() {
        assertThrowsExactly(IllegalArgumentException.class, () -> this.parser.load(this.configuration, "path"));
    }

    @Test
    void testDumpNullObject() {
        assertDoesNotThrow(() -> this.parser.dump(this.configuration, "path", null));
    }

    private static class MockClassParser extends TypedParser<MockClass> {

        public MockClassParser() {
            super(MockClass.class);
        }
    }

    private static class MockClass {

    }
}