package it.angrybear.yagl.particles;

import it.angrybear.yagl.items.Item;
import org.jetbrains.annotations.NotNull;

/**
 * An option used by {@link ParticleType#ITEM_CRACK}.
 */
public class ItemParticleOption extends ParticleOption<Item> {
    private final Item item;

    /**
     * Instantiates a new Item particle option.
     *
     * @param item the item
     */
    public ItemParticleOption(final @NotNull Item item) {
        this.item = item;
    }

    @Override
    public Item getOption() {
        return this.item;
    }
}
