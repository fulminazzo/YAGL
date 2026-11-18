package it.fulminazzo.yagl.item;

import it.fulminazzo.yagl.wrapper.Enchantment;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

/**
 * Test {@link ItemComponent} for holding enchantments.
 */
@RequiredArgsConstructor
public final class EnchantmentItemComponent implements ItemComponent {
    @Getter
    private final @NotNull Collection<Enchantment> enchantments;

    /**
     * Instantiates a new Enchantment item component.
     *
     * @param enchantments the enchantments
     */
    public EnchantmentItemComponent(final Enchantment @NotNull ... enchantments) {
        this(List.of(enchantments));
    }

}
