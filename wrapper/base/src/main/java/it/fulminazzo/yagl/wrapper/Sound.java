package it.fulminazzo.yagl.wrapper;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.Locale;

/**
 * A wrapper to represent Minecraft sounds.
 * Sounds are expressed as string, so they are <b>NOT</b> checked.
 */
@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public final class Sound extends Wrapper {
    private @NotNull String name;
    @Range(from = 0, to = 1)
    private float volume;
    @Range(from = 0, to = 2)
    private float pitch;
    /**
     * The category to play the sound for.
     * Players with the specified category set to '0' will NOT hear this sound when played.
     *
     * @return the category
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
     * @param name   the sound
     * @param volume the volume
     */
    public Sound(final @NotNull String name, final @Range(from = 0, to = 1) float volume) {
        this(name, volume, 1.0F);
    }

    /**
     * Instantiates a new Sound.
     *
     * @param name   the sound
     * @param volume the volume
     * @param pitch  the pitch
     */
    public Sound(final @NotNull String name,
                 final @Range(from = 0, to = 1) float volume,
                 final @Range(from = 0, to = 2) float pitch) {
        this(name, volume, pitch, null);
    }

    /**
     * Instantiates a new Sound.
     *
     * @param name     the sound
     * @param volume   the volume
     * @param pitch    the pitch
     * @param category the category
     */
    public Sound(final @NotNull String name,
                 final @Range(from = 0, to = 1) float volume,
                 final @Range(from = 0, to = 2) float pitch,
                 final @Nullable String category) {
        setName(name).setVolume(volume).setPitch(pitch).setCategory(category);
    }

    /**
     * Sets sound.
     *
     * @param name the sound
     * @return this sound
     */
    public @NotNull Sound setName(final @NotNull String name) {
        this.name = name.toLowerCase(Locale.ROOT);
        return this;
    }

    /**
     * Sets volume.
     * It is limited from 0 and 1.0, where 1.0 is the loudest and 0.0 is the quietest.
     * Setting this value to higher than its limit will make sure that
     * it can be heard at maximum volume even at longer distances.
     *
     * @param volume the volume
     * @return this sound
     */
    public @NotNull Sound setVolume(final @Range(from = 0, to = 1) float volume) {
        this.volume = check("volume", volume, 0);
        return this;
    }

    /**
     * Sets the pitch.
     * It is limited from 0 and 2.0, where 2.0 is the fastest and 0.0 is the lowest.
     * The default value is 1.0.
     *
     * @param pitch the pitch
     * @return this sound
     */
    public @NotNull Sound setPitch(final @Range(from = 0, to = 2) float pitch) {
        this.pitch = check("pitch", pitch, 0);
        return this;
    }

    /**
     * Sets the category of this sound.
     *
     * @param category the category
     * @return this sound
     */
    public @NotNull Sound setCategory(final @Nullable String category) {
        this.category = category;
        return this;
    }

}
