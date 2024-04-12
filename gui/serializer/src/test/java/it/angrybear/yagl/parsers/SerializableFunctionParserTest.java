package it.angrybear.yagl.parsers;

import it.angrybear.yagl.ParserTestHelper;
import it.angrybear.yagl.SerializableFunction;

class SerializableFunctionParserTest extends ParserTestHelper<SerializableFunctionParserTest.MockFunction> {

    @Override
    protected Class<?> getParser() {
        return MockFunctionParser.class;
    }

    static class MockFunctionParser extends SerializableFunctionParser<MockFunction> {

        public MockFunctionParser() {
            super(MockFunction.class);
        }

    }

    static class MockFunction implements SerializableFunction {

    }
}