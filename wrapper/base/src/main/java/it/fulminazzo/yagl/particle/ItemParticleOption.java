package it.fulminazzo.yagl.particle;

import it.fulminazzo.yagl.item.AbstractItem;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

/**
 * An option used by {@link ParticleType#ITEM_CRACK}.
 */
@SuppressWarnings("deprecation")
@EqualsAndHashCode(callSuper = false)
@ToString
public final class ItemParticleOption<I extends AbstractItem> extends ParticleOption<I> {
    private final @NotNull I item;

    /**
     * Instantiates a new Item particle option.
     *
     * @param item the item
     */
    public ItemParticleOption(final @NotNull I item) {
        this.item = item;
    }

    @Override
    public @NotNull I getOption() {
        return this.item;
    }

}
