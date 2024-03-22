package it.angrybear.yagl.actions;

import it.fulminazzo.fulmicollection.utils.SerializeUtils;

import java.io.Serializable;

/**
 * A general interface for various actions.
 * Provides a {@link #serialize()} method for serializing purposes.
 */
public interface Action extends Serializable {

    /**
     * Serialize.
     *
     * @return this action serialized
     */
    default String serialize() {
        return SerializeUtils.serializeToBase64(this);
    }
}
