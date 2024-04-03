package it.angrybear.yagl.particles;

import it.angrybear.yagl.items.AbstractItem;
import org.jetbrains.annotations.NotNull;

/**
 * An option used by {@link ParticleType#ITEM_CRACK}.
 */
@SuppressWarnings("deprecation")
public class ItemParticleOption<I extends AbstractItem> extends ParticleOption<I> {
    private final I item;

    /**
     * Instantiates a new Item particle option.
     *
     * @param item the item
     */
    public ItemParticleOption(final @NotNull I item) {
        this.item = item;
    }

    @Override
    public I getOption() {
        return this.item;
    }
}
