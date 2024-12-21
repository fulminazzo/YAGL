package it.fulminazzo.yagl;

import lombok.Getter;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;

/**
 * A wrapper for {@link Potion} objects.
 */
@Getter
public class PotionWrapper {
    private final Potion internalPotion;

    /**
     * Instantiates a new Potion wrapper.
     *
     * @param potionType the potion type
     * @param level      the level
     * @param splash     the splash
     * @param extended   the extended
     */
    public PotionWrapper(final @NotNull PotionType potionType, final int level,
                         final boolean splash, final boolean extended) {
        this.internalPotion = new Potion(potionType, level, splash, extended);
    }

    /**
     * Instantiates a new Potion wrapper.
     *
     * @param potion the potion (must be of type {@link Potion}
     */
    public <P> PotionWrapper(final @NotNull P potion) {
        if (potion instanceof Potion) this.internalPotion = (Potion) potion;
        else throw new IllegalArgumentException("Expected class org.bukkit.potion.Potion but instead got " +
                potion.getClass().getCanonicalName());
    }

    /**
     * Returns the {@link PotionType} of this potion.
     *
     * @return The type of this potion
     */
    public PotionType getType() {
        return this.internalPotion.getType();
    }

    /**
     * Returns the level of this potion.
     *
     * @return The level of this potion
     */
    public int getLevel() {
        return this.internalPotion.getLevel();
    }

    /**
     * Returns whether this potion is a splash potion.
     *
     * @return Whether this is a splash potion
     */
    public boolean isSplash() {
        return this.internalPotion.isSplash();
    }

    /**
     * Returns whether this potion has an extended duration.
     *
     * @return Whether this potion has extended duration
     */
    public boolean hasExtendedDuration() {
        return this.internalPotion.hasExtendedDuration();
    }

}
