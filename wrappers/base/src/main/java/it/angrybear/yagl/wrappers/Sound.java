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
    private @NotNull String sound;
    private float volume;
    private float pitch;

    /**
     * Instantiates a new Sound.
     *
     * @param sound the sound
     */
    public Sound(final @NotNull String sound) {
        this(sound, 1.0F);
    }

    /**
     * Instantiates a new Sound.
     *
     * @param sound  the sound
     * @param volume the volume
     */
    public Sound(final @NotNull String sound, final float volume) {
        this(sound, volume, 1.0F);
    }

    /**
     * Instantiates a new Sound.
     *
     * @param sound  the sound
     * @param volume the volume
     * @param pitch  the pitch
     */
    public Sound(final @NotNull String sound, final float volume, final float pitch) {
        this.sound = sound;
        setVolume(volume);
        setPitch(pitch);
    }

    /**
     * Sets sound.
     *
     * @param sound the sound
     * @return this sound
     */
    public Sound setSound(final @NotNull String sound) {
        this.sound = sound;
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
     * Compare this sound with the given one
     *
     * @param sound the sound
     * @return true, if they have the same sound (despite volumes and pitches)
     */
    public boolean isSimilar(final @Nullable Sound sound) {
        if (sound == null) return false;
        return this.sound.equalsIgnoreCase(sound.getSound());
    }
}
