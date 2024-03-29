package it.angrybear.yagl.wrappers;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A wrapper to represent Minecraft sounds.
 * Sounds are expressed as string, so they are <b>NOT</b> checked.
 */
@Getter
public class Sound extends Wrapper {
    private @NotNull String name;
    private float volume;
    private float pitch;
    /**
     * The category to play the sound for.
     * Players with the specified category set to '0' will NOT hear this sound when played.
     */
    private @Nullable String category;

    /**
     * Instantiates a new Sound.
     *
     * @param name the sound
     */
    public Sound(final @NotNull String name) {
        this(name, 1.0F);
    }

    /**
     * Instantiates a new Sound.
     *
     * @param name  the sound
     * @param volume the volume
     */
    public Sound(final @NotNull String name, final float volume) {
        this(name, volume, 1.0F);
    }

    /**
     * Instantiates a new Sound.
     *
     * @param name  the sound
     * @param volume the volume
     * @param pitch  the pitch
     */
    public Sound(final @NotNull String name, final float volume, final float pitch) {
        this(name, volume, pitch, null);
    }

    /**
     * Instantiates a new Sound.
     *
     * @param name    the sound
     * @param volume   the volume
     * @param pitch    the pitch
     * @param category the category
     */
    public Sound(final @NotNull String name, final float volume, final float pitch, final @Nullable String category) {
        this.name = name;
        setVolume(volume);
        setPitch(pitch);
        this.category = category;
    }

    /**
     * Sets sound.
     *
     * @param name the sound
     * @return this sound
     */
    public Sound setName(final @NotNull String name) {
        this.name = name;
        return this;
    }

    /**
     * Sets volume.
     * It is limited from 0 and 1.0, where 1.0 is the loudest and 0.0 is the quietest.
     * Setting this value to higher than its limit, will make sure that it can be heard at maximum volume even at longer distances.
     *
     * @param volume the volume
     * @return this sound
     */
    public Sound setVolume(final float volume) {
        if (volume < 0) throw new IllegalArgumentException("Volume cannot be lower than 0");
        this.volume = volume;
        return this;
    }

    /**
     * Sets pitch.
     * It is limited from 0 and 2.0, where 2.0 is the fastest and 0.0 is the lowest.
     * The default value is 1.0.
     *
     * @param pitch the pitch
     * @return this sound
     */
    public Sound setPitch(final float pitch) {
        if (pitch < 0) throw new IllegalArgumentException("Pitch cannot be lower than 0");
        this.pitch = pitch;
        return this;
    }

    /**
     * Sets category.
     *
     * @param category the category
     * @return this sound
     */
    public Sound setCategory(final @NotNull String category) {
        this.category = category;
        return this;
    }
}
