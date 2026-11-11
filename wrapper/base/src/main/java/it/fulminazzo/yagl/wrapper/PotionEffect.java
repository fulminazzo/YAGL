package it.fulminazzo.yagl.wrapper;

import it.fulminazzo.yagl.Constants;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.Locale;

/**
 * A wrapper to represent Minecraft potion effects.
 * Effects are expressed as string, so they are <b>NOT</b> checked.
 */
@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public final class PotionEffect extends Wrapper {
    private @NotNull String name;
    @Range(from = 0, to = Long.MAX_VALUE)
    private double duration;
    @Range(from = 1, to = Integer.MAX_VALUE)
    private int amplifier;
    private boolean showingParticles;
    private boolean showingIcon;

    /**
     * Instantiates a new Potion effect.
     *
     * @param name the effect
     */
    public PotionEffect(final @NotNull String name) {
        this(name, 1);
    }

    /**
     * Instantiates a new Potion effect.
     *
     * @param name     the effect
     * @param duration the duration
     */
    public PotionEffect(final @NotNull String name, final @Range(from = 0, to = Long.MAX_VALUE) double duration) {
        this(name, duration, 1);
    }

    /**
     * Instantiates a new Potion effect.
     *
     * @param name      the effect
     * @param duration  the duration
     * @param amplifier the amplifier
     */
    public PotionEffect(final @NotNull String name,
                        final @Range(from = 0, to = Long.MAX_VALUE) double duration,
                        final @Range(from = 1, to = Integer.MAX_VALUE) int amplifier) {
        this(name, duration, amplifier, true, true);
    }

    /**
     * Instantiates a new Potion effect.
     *
     * @param name            the effect
     * @param duration        the duration
     * @param amplifier       the amplifier
     * @param enableParticles if {@code true}, the effect will show the associated particles
     */
    public PotionEffect(final @NotNull String name,
                        final @Range(from = 0, to = Long.MAX_VALUE) double duration,
                        final @Range(from = 1, to = Integer.MAX_VALUE) int amplifier,
                        final boolean enableParticles) {
        this(name, duration, amplifier, enableParticles, true);
    }

    /**
     * Instantiates a new Potion effect.
     *
     * @param name            the effect
     * @param duration        the duration
     * @param amplifier       the amplifier
     * @param enableParticles if {@code true}, the effect will show the associated particles
     * @param enableIcon      if {@code true}, the effect will show the associated icon in the player GUI
     */
    public PotionEffect(final @NotNull String name,
                        final @Range(from = 0, to = Long.MAX_VALUE) double duration,
                        final @Range(from = 1, to = Integer.MAX_VALUE) int amplifier,
                        final boolean enableParticles,
                        final boolean enableIcon) {
        setName(name).setDuration(duration).setAmplifier(amplifier);
        this.showingParticles = enableParticles;
        this.showingIcon = enableIcon;
    }

    /**
     * Sets effect.
     *
     * @param name the effect
     * @return this potion effect
     */
    public @NotNull PotionEffect setName(final @NotNull String name) {
        this.name = name.toLowerCase(Locale.ROOT);
        return this;
    }

    /**
     * Gets duration in ticks.
     *
     * @return the duration in ticks
     */
    public @Range(from = 0, to = Integer.MAX_VALUE) int getDurationInTicks() {
        return (int) (getDuration() * Constants.TICKS_IN_SECOND);
    }

    /**
     * Sets duration.
     *
     * @param duration the duration
     * @return this potion effect
     */
    public @NotNull PotionEffect setDuration(final @Range(from = 0, to = Long.MAX_VALUE) double duration) {
        this.duration = check("duration", duration, 0);
        return this;
    }

    /**
     * Sets amplifier.
     *
     * @param amplifier the amplifier
     * @return this potion effect
     */
    public @NotNull PotionEffect setAmplifier(final @Range(from = 1, to = Integer.MAX_VALUE) int amplifier) {
        this.amplifier = check("amplifier", amplifier, 1);
        return this;
    }

    /**
     * Shows the particles associated with this potion effect.
     *
     * @return this potion effect
     */
    public @NotNull PotionEffect enableParticles() {
        this.showingParticles = true;
        return this;
    }

    /**
     * Hides the particles associated with this potion effect.
     *
     * @return this potion effect
     */
    public @NotNull PotionEffect disableParticles() {
        this.showingParticles = false;
        return this;
    }

    /**
     * Shows the icon associated with this potion effect.
     *
     * @return this potion effect
     */
    public @NotNull PotionEffect enableIcon() {
        this.showingIcon = true;
        return this;
    }

    /**
     * Hides the icon associated with this potion effect.
     *
     * @return this potion effect
     */
    public @NotNull PotionEffect disableIcon() {
        this.showingIcon = false;
        return this;
    }

}
