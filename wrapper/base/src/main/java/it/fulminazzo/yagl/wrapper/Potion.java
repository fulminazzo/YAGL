package it.fulminazzo.yagl.wrapper;

import it.fulminazzo.yagl.particle.LegacyParticleType;
import it.fulminazzo.yagl.util.ValidationUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.Locale;

/**
 * A {@link Wrapper} class used to represent potions in older versions of Minecraft.
 * Currently used only for {@link LegacyParticleType#POTION_BREAK}.
 */
@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public final class Potion extends Wrapper {
    private static final int MIN_LEVEL = 1;
    private static final int MAX_LEVEL = 2;

    private @NotNull String name;
    @Range(from = MIN_LEVEL, to = MAX_LEVEL)
    @Getter
    private int level;
    private boolean splash;
    private boolean extended;

    /**
     * Instantiates a new Potion.
     *
     * @param name the type
     */
    public Potion(final @NotNull String name) {
        this(name, MIN_LEVEL);
    }

    /**
     * Instantiates a new Potion.
     *
     * @param name  the type
     * @param level the level
     */
    public Potion(final @NotNull String name, final @Range(from = MIN_LEVEL, to = MAX_LEVEL) int level) {
        this(name, level, false, false);
    }

    /**
     * Instantiates a new Potion.
     *
     * @param name   the type
     * @param level  the level
     * @param splash if {@code true} the potion is throwable
     */
    public Potion(final @NotNull String name, final @Range(from = MIN_LEVEL, to = MAX_LEVEL) int level, final boolean splash) {
        this(name, level, splash, false);
    }

    /**
     * Instantiates a new Potion.
     *
     * @param name     the type
     * @param level    the level
     * @param splash   if {@code true} the potion is throwable
     * @param extended if {@code true} the duration is extended
     */
    public Potion(final @NotNull String name,
                  final @Range(from = MIN_LEVEL, to = MAX_LEVEL) int level,
                  final boolean splash,
                  final boolean extended) {
        setName(name).setLevel(level);
        this.splash = splash;
        this.extended = extended;
    }

    /**
     * Sets type.
     *
     * @param name the type
     * @return the type
     */
    public @NotNull Potion setName(final @NotNull String name) {
        this.name = name.toLowerCase(Locale.ROOT);
        return this;
    }

    /**
     * Sets level.
     *
     * @param level the level
     * @return this potion
     */
    public @NotNull Potion setLevel(final @Range(from = MIN_LEVEL, to = MAX_LEVEL) int level) {
        ValidationUtils.checkRange("level", level, MIN_LEVEL, MAX_LEVEL);
        this.level = level;
        return this;
    }

    /**
     * Enables the potion to be throwable.
     *
     * @return this potion
     */
    public @NotNull Potion setSplash() {
        this.splash = true;
        return this;
    }

    /**
     * Disables the potion from being throwable.
     *
     * @return this potion
     */
    public @NotNull Potion unsetSplash() {
        this.splash = false;
        return this;
    }

    /**
     * Extends the duration of the potion.
     *
     * @return this potion
     */
    public @NotNull Potion setExtended() {
        this.extended = true;
        return this;
    }

    /**
     * Shrinks the duration of the potion.
     *
     * @return this potion
     */
    public @NotNull Potion unsetExtended() {
        this.extended = false;
        return this;
    }

}
