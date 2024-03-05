package it.angrybear.yagl.items.recipes;

import it.angrybear.yagl.items.Item;
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

    /**
     * Sets ingredient.
     *
     * @param ingredient the ingredient
     */
    public void setIngredient(final @NotNull Item ingredient) {
        this.ingredient = ingredient.copy(Item.class);
    }

    @Override
    public void setOutput(final @NotNull Item output) {
        this.output = output.copy(Item.class);
    }

    @Override
    public List<Item> getIngredients() {
        return Collections.singletonList(this.ingredient);
    }
}
