package it.angrybear.yagl.items.recipes;

import it.angrybear.yagl.items.Item;
import it.fulminazzo.fulmicollection.objects.FieldEquable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

/**
 * A basic implementation of {@link Recipe}.
 */
@Getter
@EqualsAndHashCode
@ToString
abstract class RecipeImpl implements Recipe {
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
