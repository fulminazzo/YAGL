package it.fulminazzo.yagl.metadatable;

import java.util.Collections;

/**
 * A special implementation of {@link MetadatableHelper}
 * that allows to apply PlaceholderAPI variables to the specified object.
 */
public final class PAPIParser extends MetadatableHelper {

    /**
     * Instantiates a new PAPIParser helper.
     */
    PAPIParser() {
        super(Collections::emptyMap);
    }

}
