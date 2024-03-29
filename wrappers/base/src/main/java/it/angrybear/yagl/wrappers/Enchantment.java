package it.angrybear.yagl.wrappers;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A wrapper to represent Minecraft enchantments.
 * Enchantments are expressed as string, so they are <b>NOT</b> checked.
 */
@Getter
public class Enchantment extends Wrapper {
    private @NotNull String enchantment;
    private int level;

    /**
     * Instantiates a new Enchantment.
     *
     * @param enchantment the enchantment
     */
    public Enchantment(final @NotNull String enchantment) {
        this(enchantment, 1);
    }

    /**
     * Instantiates a new Enchantment.
     *
     * @param enchantment the enchantment
     * @param level       the level (1 is the lowest)
     */
    public Enchantment(final @NotNull String enchantment, final int level) {
        this.enchantment = enchantment;
        setLevel(level);
    }

    /**
     * Sets enchantment.
     *
     * @param enchantment the enchantment
     * @return this enchantment
     */
    public Enchantment setEnchantment(final @NotNull String enchantment) {
        this.enchantment = enchantment;
        return this;
    }

    /**
     * Sets level.
     *
     * @param level the level (1 is the lowest)
     * @return this enchantment
     */
    public Enchantment setLevel(final int level) {
        if (level < 1) throw new IllegalArgumentException("Level cannot be lower than 1");
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
        if (enchantment == null) return false;
        return this.enchantment.equalsIgnoreCase(enchantment.getEnchantment()) && this.level <= enchantment.getLevel();
    }
}
