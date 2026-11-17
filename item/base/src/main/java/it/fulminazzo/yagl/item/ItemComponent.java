package it.fulminazzo.yagl.item;

/**
 * ItemComponent is a marker interface that allows modification of the item metadata
 * which is not supported by YAGL.
 * <br>
 * While {@link Item} provides several methods to interact with metadata (name, lore, enchantments, etc...),
 * there are still many left out special cases that require custom handling.
 * <br>
 * As an example, let's consider a "PLAYER_HEAD" item, which can have a custom owner set in order to change the skin.
 * In this case, one example of implementation might be:
 * <pre>{@code
 * public class HeadComponent implements ItemComponent {
 *     private final String owner;
 *
 *     public HeadComponent(final String owner) {
 *         this.owner = owner;
 *     }
 *
 *     public String getOwner() {
 *         return owner;
 *     }
 *
 * }
 * }</pre>
 * And apply it to an item using {@link Item#setComponent}.
 * <br>
 * <b>NOTE</b>: components are unique based on their type (an item can have only one HeadComponent).
 *
 * @see Item
 */
public interface ItemComponent {
    // empty on purpose

}
