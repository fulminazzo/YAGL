package it.angrybear.items.recipes;

import it.angrybear.items.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * An implementation of {@link Recipe} to express any recipe in the furnace in Minecraft.
 */
@Getter
@Setter
public class FurnaceRecipe implements Recipe {
    @Getter
    private final String id;
    @Getter(AccessLevel.NONE)
    private Item ingredient;
    private Item output;
    private float experience;
    private int cookingTime;

    /**
     * Instantiates a new Furnace recipe.
     *
     * @param id the id
     */
    public FurnaceRecipe(final @NotNull String id) {
        this.id = id;
    }

    @Override
    public List<Item> getIngredients() {
        return Collections.singletonList(this.ingredient);
    }
}
