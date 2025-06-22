package it.fulminazzo.yagl.parsers;

import it.fulminazzo.yagl.contents.GUIContent;

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
