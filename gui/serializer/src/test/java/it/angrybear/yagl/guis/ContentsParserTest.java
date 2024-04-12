package it.angrybear.yagl.guis;

import it.angrybear.yagl.ParserTestHelper;

class ContentsParserTest extends ParserTestHelper<GUIImpl.Contents> {

    @Override
    protected Class<?> getParser() {
        return ContentsParser.class;
    }
}