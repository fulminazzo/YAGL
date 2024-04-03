package it.angrybear.yagl.wrappers;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * A wrapper to represent Minecraft potion effects.
 * Effects are expressed as string, so they are <b>NOT</b> checked.
 */
@Getter
public class PotionEffect extends Wrapper {
    private @NotNull String name;
    @Range(min = 0)
    private double duration;
    @Range(min = 1)
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
     * @param name   the effect
     * @param duration the duration
     */
    public PotionEffect(final @NotNull String name, final double duration) {
        this(name, duration, 1);
    }

    /**
     * Instantiates a new Potion effect.
     *
     * @param name    the effect
     * @param duration  the duration
     * @param amplifier the amplifier
     */
    public PotionEffect(final @NotNull String name, final double duration, final int amplifier) {
        this(name, duration, amplifier, true, true);
    }

    /**
     * Instantiates a new Potion effect.
     *
     * @param name          the effect
     * @param duration        the duration
     * @param amplifier       the amplifier
     * @param enableParticles the enable particles
     */
    public PotionEffect(final @NotNull String name, final double duration, final int amplifier,
                        final boolean enableParticles) {
        this(name, duration, amplifier, enableParticles, true);
    }

    /**
     * Instantiates a new Potion effect.
     *
     * @param name          the effect
     * @param duration        the duration
     * @param amplifier       the amplifier
     * @param enableParticles show particles
     * @param enableIcon      show icon
     */
    public PotionEffect(final @NotNull String name, final double duration, final int amplifier,
                        final boolean enableParticles, final boolean enableIcon) {
        this.name = name;
        setDuration(duration);
        setAmplifier(amplifier);
        this.showingParticles = enableParticles;
        this.showingIcon = enableIcon;
    }

    /**
     * Sets effect.
     *
     * @param name the effect
     * @return this potion effect
     */
    public PotionEffect setName(final @NotNull String name) {
        this.name = name;
        return this;
    }

    /**
     * Gets duration in ticks.
     *
     * @return the duration in ticks
     */
    public int getDurationInTicks() {
        return (int) (getDuration() * 20);
    }

    /**
     * Sets duration.
     *
     * @param duration the duration
     * @return this potion effect
     */
    public PotionEffect setDuration(final double duration) {
        this.duration = check(duration);
        return this;
    }

    /**
     * Sets amplifier.
     *
     * @param amplifier the amplifier
     * @return this potion effect
     */
    public PotionEffect setAmplifier(final int amplifier) {
        this.amplifier = check(amplifier);
        return this;
    }

    /**
     * Gets amplifier.
     *
     * @return the amplifier
     */
    public int getAmplifier() {
        return this.amplifier - 1;
    }

    /**
     * Enable particles.
     *
     * @return this potion effect
     */
    public PotionEffect enableParticles() {
        this.showingParticles = true;
        return this;
    }

    /**
     * Disable particles.
     *
     * @return this potion effect
     */
    public PotionEffect disableParticles() {
        this.showingParticles = false;
        return this;
    }

    /**
     * Enable icon.
     *
     * @return this potion effect
     */
    public PotionEffect enableIcon() {
        this.showingIcon = true;
        return this;
    }

    /**
     * Disable icon.
     *
     * @return this potion effect
     */
    public PotionEffect disableIcon() {
        this.showingIcon = false;
        return this;
    }
}
