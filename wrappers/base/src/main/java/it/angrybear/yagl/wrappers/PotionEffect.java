package it.angrybear.yagl.wrappers;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A wrapper to represent Minecraft potion effects.
 * Effects are expressed as string, so they are <b>NOT</b> checked.
 */
@Getter
public class PotionEffect extends Wrapper {
    private @NotNull String effect;
    private int duration;
    private int amplifier;
    private boolean showingParticles;
    private boolean showingIcon;

    /**
     * Instantiates a new Potion effect.
     *
     * @param effect the effect
     */
    public PotionEffect(final @NotNull String effect) {
        this(effect, 1);
    }

    /**
     * Instantiates a new Potion effect.
     *
     * @param effect   the effect
     * @param duration the duration
     */
    public PotionEffect(final @NotNull String effect, final int duration) {
        this(effect, duration, 0);
    }

    /**
     * Instantiates a new Potion effect.
     *
     * @param effect    the effect
     * @param duration  the duration
     * @param amplifier the amplifier
     */
    public PotionEffect(final @NotNull String effect, final int duration, final int amplifier) {
        this(effect, duration, amplifier, true, true);
    }

    /**
     * Instantiates a new Potion effect.
     *
     * @param effect          the effect
     * @param duration        the duration
     * @param amplifier       the amplifier
     * @param enableParticles the enable particles
     */
    public PotionEffect(final @NotNull String effect, final int duration, final int amplifier,
                        final boolean enableParticles) {
        this(effect, duration, amplifier, enableParticles, true);
    }

    /**
     * Instantiates a new Potion effect.
     *
     * @param effect          the effect
     * @param duration        the duration
     * @param amplifier       the amplifier
     * @param enableParticles show particles
     * @param enableIcon      show icon
     */
    public PotionEffect(final @NotNull String effect, final int duration, final int amplifier,
                        final boolean enableParticles, final boolean enableIcon) {
        this.effect = effect;
        setDuration(duration);
        setAmplifier(amplifier);
        this.showingParticles = enableParticles;
        this.showingIcon = enableIcon;
    }

    /**
     * Sets effect.
     *
     * @param effect the effect
     * @return this potion effect
     */
    public PotionEffect setEffect(final @NotNull String effect) {
        this.effect = effect;
        return this;
    }

    /**
     * Gets duration in ticks.
     *
     * @return the duration in ticks
     */
    public int getDurationInTicks() {
        return getDuration() * 20;
    }

    /**
     * Sets duration.
     *
     * @param duration the duration
     * @return this potion effect
     */
    public PotionEffect setDuration(final int duration) {
        if (duration < 0) throw new IllegalArgumentException("Duration cannot be lower than 0");
        this.duration = duration;
        return this;
    }

    /**
     * Sets amplifier.
     *
     * @param amplifier the amplifier
     * @return this potion effect
     */
    public PotionEffect setAmplifier(final int amplifier) {
        if (amplifier < 1) throw new IllegalArgumentException("Amplifier cannot be lower than 1");
        this.amplifier = amplifier;
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

    /**
     * Compare this potion effect with the given one
     *
     * @param potionEffect the potion effect
     * @return true, if they have the same potion effect
     */
    public boolean isSimilar(final @Nullable PotionEffect potionEffect) {
        if (potionEffect == null) return false;
        return this.effect.equalsIgnoreCase(potionEffect.getEffect());
    }
}
