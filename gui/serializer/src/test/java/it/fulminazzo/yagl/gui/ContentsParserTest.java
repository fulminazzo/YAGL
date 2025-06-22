package it.fulminazzo.yagl.gui;

import it.fulminazzo.yagl.ParserTestHelper;
import it.fulminazzo.yamlparser.configuration.IConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class ContentsParserTest extends ParserTestHelper<GUIImpl.Contents> {

    @Override
    protected Class<?> getParser() {
        return ContentsParser.class;
    }

    @Override
    protected @NotNull IConfiguration getConfiguration(@Nullable String returnValue) {
        IConfiguration configuration = super.getConfiguration(returnValue);
        when(configuration.getList(anyString(), any())).thenReturn(null);
        return configuration;
    }

}