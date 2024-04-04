package it.angrybear.yagl.items.recipes;

import it.angrybear.yagl.items.Item;
import it.angrybear.yagl.wrappers.Range;
import it.angrybear.yagl.wrappers.Wrapper;
import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * An implementation of {@link Recipe} to express any recipe in the furnace in Minecraft.
 */
@Getter
public class FurnaceRecipe extends RecipeImpl {
    @Getter(AccessLevel.NONE)
    private Item ingredient;
    @Range(min = 0)
    private float experience;
    @Range(min = 0)
    private double cookingTime;

    private FurnaceRecipe() {
        this("pending");
    }

    /**
     * Instantiates a new Furnace recipe.
     *
     * @param id the id
     */
    public FurnaceRecipe(final @NotNull String id) {
        super(id);
    }

    /**
     * Sets ingredient.
     *
     * @param ingredient the ingredient
     * @return this recipe
     */
    public @NotNull FurnaceRecipe setIngredient(final @NotNull Item ingredient) {
        this.ingredient = ingredient.copy(Item.class);
        return this;
    }

    /**
     * Sets experience.
     *
     * @param experience the experience
     * @return this recipe
     */
    public @NotNull FurnaceRecipe setExperience(final float experience) {
        this.experience = Wrapper.check(this, experience);
        return this;
    }

    /**
     * Sets cooking time in seconds.
     *
     * @param cookingTime the cooking time
     * @return this recipe
     */
    public @NotNull FurnaceRecipe setCookingTime(final double cookingTime) {
        this.cookingTime = Wrapper.check(this, cookingTime);
        return this;
    }

    @Override
    public @NotNull FurnaceRecipe setOutput(final @NotNull Item output) {
        this.output = output.copy(Item.class);
        return this;
    }

    @Override
    public @NotNull List<Item> getIngredients() {
        return Collections.singletonList(this.ingredient);
    }
}
