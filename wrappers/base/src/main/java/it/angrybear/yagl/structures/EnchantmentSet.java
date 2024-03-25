package it.angrybear.yagl.structures;

import it.angrybear.yagl.wrappers.Enchantment;

public class EnchantmentSet extends PComparatorSet<Enchantment> {

    public EnchantmentSet() {
        super(Enchantment::compareLevels, Enchantment::isSimilar);
    }
}
