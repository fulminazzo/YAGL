package it.angrybear.items;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public interface Item {

    Item setMaterial(final @NotNull String material);

    @NotNull String getMaterial();

    Item setAmount(final int amount);

    int getAmount();

    Item setDurability(final short durability);

    short getDurability();

    Item setDisplayName(final @NotNull String displayName);

    @NotNull String getDisplayName();

    default Item setLore(final String @NotNull ... lore) {
        return setLore(Arrays.asList(lore));
    }

    Item setLore(final @NotNull Collection<String> lore);

    @NotNull List<String> getLore();

    //TODO:
    boolean isSimilar();
}
