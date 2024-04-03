package it.angrybear.yagl.particles;

import it.angrybear.yagl.items.AbstractItem;
import org.jetbrains.annotations.NotNull;

/**
 * An option used by {@link ParticleType#ITEM_CRACK}.
 */
public class ItemParticleOption extends ParticleOption<AbstractItem> {
    private final AbstractItem item;

    /**
     * Instantiates a new Item particle option.
     *
     * @param item the item
     */
    public ItemParticleOption(final @NotNull AbstractItem item) {
        this.item = item;
    }

    @Override
    public AbstractItem getOption() {
        return this.item;
    }
}
