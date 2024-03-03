package it.angrybear.items;

import it.fulminazzo.fulmicollection.objects.Printable;
import it.fulminazzo.fulmicollection.utils.ReflectionUtils;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A wrapper to represent Minecraft enchantments.
 * Enchantments are expressed as string, so they are <b>NOT</b> checked.
 */
@Getter
public class Enchantment {
    private @NotNull String enchantment;
    private int level;

    /**
     * Instantiates a new Enchantment.
     *
     * @param enchantment the enchantment
     */
    public Enchantment(final @NotNull String enchantment) {
        this(enchantment, 0);
    }

    /**
     * Instantiates a new Enchantment.
     *
     * @param enchantment the enchantment
     * @param level       the level (0 is the lowest)
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
     * @param level the level
     * @return this level
     */
    public Enchantment setLevel(final int level) {
        if (level < 0) throw new IllegalArgumentException("Level cannot be lower than 0");
        this.level = level;
        return this;
    }

    /**
     * Compare this enchantment with the given one.
     *
     * @param enchantment the enchantment
     * @return true if they are equal
     */
    public boolean equals(final @Nullable Enchantment enchantment) {
        if (enchantment == null) return false;
        return ReflectionUtils.equalsFields(this, enchantment);
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public String toString() {
        return Printable.convertToJson(this);
    }
}
