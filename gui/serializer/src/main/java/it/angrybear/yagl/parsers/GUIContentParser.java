package it.angrybear.yagl.parsers;

import it.angrybear.yagl.contents.GUIContent;

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
