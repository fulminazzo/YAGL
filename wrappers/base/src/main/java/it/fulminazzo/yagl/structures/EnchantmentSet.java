package it.fulminazzo.yagl.structures;

import it.fulminazzo.yagl.wrappers.Enchantment;
import it.fulminazzo.yagl.wrappers.Wrapper;

/**
 * A {@link java.util.Set} that uses {@link Enchantment#compareLevels(Enchantment)} and
 * {@link Enchantment#isSimilar(Wrapper)} to compare enchantments.
 * One is added only if the previous two are false.
 */
public final class EnchantmentSet extends PComparatorSet<Enchantment> {

    /**
     * Instantiates a new Enchantment set.
     */
    public EnchantmentSet() {
        super(Enchantment::compareLevels, Enchantment::isSimilar);
    }
}
