package it.fulminazzo.yagl;

import it.fulminazzo.yagl.item.ItemComponent;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

/**
 * Interface for converting {@link ItemComponent} to {@link ItemMeta} representations.
 * <br>
 * Following the same example in {@link ItemComponent},
 * an adapter for {@code HeadComponent} could be the following:
 * <pre>{@code
 * class HeadComponentAdapter extends ItemComponentAdapter<HeadComponent, SkullMeta> {
 *
 *     @Override
 *     public @NotNull HeadComponent extract(@NotNull SkullMeta meta) {
 *         // Extract data from the meta and recreate the component.
 *         return new HeadComponent(meta.getOwner());
 *     }
 *
 *     @Override
 *     public void apply(@NotNull HeadComponent component, @NotNull SkullMeta meta) {
 *         // Apply the component data to the given metadata.
 *         meta.setOwner(component.getOwner());
 *     }
 *
 *     @Override
 *     public @NotNull Class<SkullMeta> getMetaType() {
 *         // The metadata type required in order for the component to work.
 *         // Dynamically determined based on the item material.
 *         return SkullMeta.class;
 *     }
 *
 * }
 * }</pre>
 * <br>
 * Make sure the adapter is registered with {@link ItemAdapter#registerComponentAdapter} before using it.
 *
 * @param <C> the type of the component
 * @param <M> the type of the metadata
 * @see ItemComponent
 * @see ItemAdapter
 */
public interface ItemComponentAdapter<C extends ItemComponent, M extends ItemMeta> {

    /**
     * Extracts the component data from the given metadata.
     *
     * @param meta the metadata to extract the component data from
     * @return the extracted component
     */
    @NotNull C extract(final @NotNull M meta);

    /**
     * Applies the component data to the given metadata.
     *
     * @param component the component to apply
     * @param meta      the metadata to apply the component data to
     */
    void apply(final @NotNull C component, final @NotNull M meta);

    /**
     * Gets the metadata type required in order for the component to work.
     *
     * @return the metadata type
     */
    @NotNull Class<M> getMetaType();

}
