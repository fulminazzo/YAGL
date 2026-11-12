package it.fulminazzo.yagl.item.recipe;

import it.fulminazzo.yagl.Constants;
import it.fulminazzo.yagl.item.Item;
import it.fulminazzo.yagl.util.ValidationUtils;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.Collections;
import java.util.List;

/**
 * An implementation of {@link Recipe} to express any recipe in the furnace in Minecraft.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public final class FurnaceRecipe extends RecipeImpl {
    @Getter(AccessLevel.NONE)
    private Item ingredient;
    @Range(from = 0, to = Long.MAX_VALUE)
    private float experience;
    @Range(from = 0, to = Long.MAX_VALUE)
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
    public @NotNull FurnaceRecipe setExperience(final @Range(from = 0, to = Long.MAX_VALUE) float experience) {
        ValidationUtils.checkMin("experience", experience, 0);
        this.experience = experience;
        return this;
    }

    /**
     * Gets cooking time in ticks.
     *
     * @return the cooking time
     */
    public @Range(from = 0, to = Long.MAX_VALUE) long getCookingTimeInTicks() {
        return (long) (this.cookingTime * Constants.TICKS_IN_SECOND);
    }

    /**
     * Sets cooking time in seconds.
     *
     * @param cookingTime the cooking time
     * @return this recipe
     */
    public @NotNull FurnaceRecipe setCookingTime(final @Range(from = 0, to = Long.MAX_VALUE) double cookingTime) {
        ValidationUtils.checkMin("cookingTime", cookingTime, 0);
        this.cookingTime = cookingTime;
        return this;
    }

    @Override
    public @NotNull FurnaceRecipe setOutput(final @NotNull Item output) {
        this.output = output.copy(Item.class);
        return this;
    }

    @Override
    public @NotNull List<Item> getIngredients() {
        return this.ingredient == null ? Collections.emptyList() : Collections.singletonList(this.ingredient);
    }

}
