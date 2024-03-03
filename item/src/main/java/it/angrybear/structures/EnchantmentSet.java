package it.angrybear.structures;

import it.angrybear.items.Enchantment;

/**
 * An implementation of {@link PComparatorSet} for {@link Enchantment}s.
 */
public class EnchantmentSet extends PComparatorSet<Enchantment> {

    /**
     * Instantiates a new Enchantment set.
     */
    public EnchantmentSet() {
        super(Enchantment::compareLevels, Enchantment::isSimilar);
    }
}
