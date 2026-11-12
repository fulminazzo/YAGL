package it.fulminazzo.yagl.wrapper;

import it.fulminazzo.yagl.util.ValidationUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.Locale;

/**
 * A wrapper to represent Minecraft enchantments.
 * Enchantments are expressed as string, so they are <b>NOT</b> checked.
 */
@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public final class Enchantment extends Wrapper {
    private static final int MIN_LEVEL = 1;

    private @NotNull String name;
    @Range(from = MIN_LEVEL, to = Integer.MAX_VALUE)
    private int level;

    /**
     * Instantiates a new Enchantment.
     *
     * @param name the enchantment
     */
    public Enchantment(final @NotNull String name) {
        this(name, MIN_LEVEL);
    }

    /**
     * Instantiates a new Enchantment.
     *
     * @param name  the enchantment
     * @param level the level (1 is the lowest)
     */
    public Enchantment(final @NotNull String name, final @Range(from = MIN_LEVEL, to = Integer.MAX_VALUE) int level) {
        setName(name).setLevel(level);
    }

    /**
     * Sets enchantment.
     *
     * @param name the enchantment
     * @return this enchantment
     */
    public @NotNull Enchantment setName(final @NotNull String name) {
        this.name = name.toLowerCase(Locale.ROOT);
        return this;
    }

    /**
     * Sets level.
     *
     * @param level the level (1 is the lowest)
     * @return this enchantment
     */
    public @NotNull Enchantment setLevel(final @Range(from = MIN_LEVEL, to = Integer.MAX_VALUE) int level) {
        ValidationUtils.checkMin("level", level, MIN_LEVEL);
        this.level = level;
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
