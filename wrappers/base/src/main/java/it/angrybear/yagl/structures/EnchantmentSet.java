package it.angrybear.yagl.structures;

import it.angrybear.yagl.wrappers.Enchantment;

/**
 * A {@link java.util.Set} that uses {@link Enchantment#compareLevels(Enchantment)} and
 * {@link Enchantment#isSimilar(it.angrybear.yagl.wrappers.Wrapper)} to compare enchantments.
 * One is added only if the previous two are false.
 */
public class EnchantmentSet extends PComparatorSet<Enchantment> {

    /**
     * Instantiates a new Enchantment set.
     */
    public EnchantmentSet() {
        super(Enchantment::compareLevels, Enchantment::isSimilar);
    }
}
