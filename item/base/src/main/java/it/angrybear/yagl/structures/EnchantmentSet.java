package it.angrybear.yagl.structures;

import it.angrybear.yagl.items.fields.Enchantment;

/**
 * An implementation of {@link PComparatorSet} for {@link Enchantment}s.
 */
//TODO: what do to with this? Move it to wrappers?
public class EnchantmentSet extends PComparatorSet<Enchantment> {

    /**
     * Instantiates a new Enchantment set.
     */
    public EnchantmentSet() {
        super(Enchantment::compareLevels, Enchantment::isSimilar);
    }
}
