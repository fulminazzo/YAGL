package it.fulminazzo.yagl.parser;

import it.fulminazzo.yagl.content.GUIContent;

/**
 * A parser to serialize {@link GUIContent}.
 */
public class GUIContentParser extends TypedParser<GUIContent> {

    /**
     * Instantiates a new Gui content parser.
     */
    public GUIContentParser() {
        super(GUIContent.class);
    }
}
