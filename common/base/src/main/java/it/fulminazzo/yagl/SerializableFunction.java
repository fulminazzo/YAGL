package it.fulminazzo.yagl;

import it.fulminazzo.fulmicollection.utils.SerializeUtils;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

/**
 * A general interface for various functions.
 * Provides a {@link #serialize()} method for serializing purposes.
 */
public interface SerializableFunction extends Serializable {

    /**
     * Serialize.
     *
     * @return this function serialized
     */
    default @Nullable String serialize() {
        return SerializeUtils.serializeToBase64(this);
    }
}
