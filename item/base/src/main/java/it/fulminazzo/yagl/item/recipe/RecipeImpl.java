package it.fulminazzo.yagl.item.recipe;

import it.fulminazzo.yagl.item.Item;
import it.fulminazzo.fulmicollection.objects.FieldEquable;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * A basic implementation of {@link Recipe}.
 */
@Getter
abstract class RecipeImpl extends FieldEquable implements Recipe {
    protected final @NotNull String id;
    protected Item output;

    /**
     * Instantiates a new Recipe.
     *
     * @param id the id
     */
    RecipeImpl(@NotNull String id) {
        this.id = id;
    }
}
