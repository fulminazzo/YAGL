package it.fulminazzo.yagl.wrapper;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A wrapper to represent Minecraft enchantments.
 * Enchantments are expressed as string, so they are <b>NOT</b> checked.
 */
@Getter
public final class Enchantment extends Wrapper {
    private @NotNull String name;
    @Range(min = 1)
    private int level;

    /**
     * Instantiates a new Enchantment.
     *
     * @param name the enchantment
     */
    public Enchantment(final @NotNull String name) {
        this(name, 1);
    }

    /**
     * Instantiates a new Enchantment.
     *
     * @param name the enchantment
     * @param level       the level (1 is the lowest)
     */
    public Enchantment(final @NotNull String name, final int level) {
        setName(name).setLevel(level);
    }

    /**
     * Sets enchantment.
     *
     * @param name the enchantment
     * @return this enchantment
     */
    public Enchantment setName(final @NotNull String name) {
        this.name = name.toLowerCase();
        return this;
    }

    /**
     * Sets level.
     *
     * @param level the level (1 is the lowest)
     * @return this enchantment
     */
    public Enchantment setLevel(final int level) {
        this.level = check(level);
        return this;
    }

    /**
     * Compare this enchantment with the given one
     *
     * @param enchantment the enchantment
     * @return true, if they have the same enchantment and the second one has a level higher or equal than the current
     */
    public boolean compareLevels(final @Nullable Enchantment enchantment) {
        return enchantment != null && this.name.equalsIgnoreCase(enchantment.getName()) &&
                this.level <= enchantment.getLevel();
    }
}
