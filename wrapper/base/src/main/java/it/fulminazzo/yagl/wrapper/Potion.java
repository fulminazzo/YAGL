package it.fulminazzo.yagl.wrapper;

import it.fulminazzo.yagl.particle.LegacyParticleType;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link Wrapper} class used to represent potions in older versions of Minecraft.
 * Currently used only for {@link LegacyParticleType#POTION_BREAK}.
 */
@Getter
public final class Potion extends Wrapper {
    private @NotNull String name;
    @Range(min = 1, max = 2)
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
        this(name, 1);
    }

    /**
     * Instantiates a new Potion.
     *
     * @param name  the type
     * @param level the level
     */
    public Potion(final @NotNull String name, final int level) {
        this(name, level, false, false);
    }

    /**
     * Instantiates a new Potion.
     *
     * @param name   the type
     * @param level  the level
     * @param splash the splash
     */
    public Potion(final @NotNull String name, final int level, final boolean splash) {
        this(name, level, splash, false);
    }

    /**
     * Instantiates a new Potion.
     *
     * @param name     the type
     * @param level    the level
     * @param splash   the splash
     * @param extended the extended
     */
    public Potion(final @NotNull String name, final int level, final boolean splash, final boolean extended) {
        this.name = name;
        setLevel(level);
        this.splash = splash;
        this.extended = extended;
    }

    /**
     * Sets type.
     *
     * @param name the type
     * @return the type
     */
    public Potion setName(final @NotNull String name) {
        this.name = name;
        return this;
    }

    /**
     * Sets level.
     *
     * @param level the level
     * @return this potion
     */
    public Potion setLevel(final int level) {
        this.level = check(level);
        return this;
    }

    /**
     * Sets splash.
     *
     * @return this potion
     */
    public Potion setSplash() {
        this.splash = true;
        return this;
    }

    /**
     * Unset splash potion.
     *
     * @return this potion
     */
    public Potion unsetSplash() {
        this.splash = false;
        return this;
    }

    /**
     * Sets extended.
     *
     * @return this potion
     */
    public Potion setExtended() {
        this.extended = true;
        return this;
    }

    /**
     * Unset extended potion.
     *
     * @return this potion
     */
    public Potion unsetExtended() {
        this.extended = false;
        return this;
    }

}
