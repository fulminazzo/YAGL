package it.angrybear.yagl.items.recipes;

import it.angrybear.yagl.items.Item;
import it.fulminazzo.fulmicollection.utils.ReflectionUtils;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * A basic implementation of {@link Recipe}.
 */
@Getter
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

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RecipeImpl) return ReflectionUtils.equalsFields(this, obj);
        return super.equals(obj);
    }
}
