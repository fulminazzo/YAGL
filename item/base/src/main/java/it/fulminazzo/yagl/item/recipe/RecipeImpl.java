package it.fulminazzo.yagl.item.recipe;

import it.fulminazzo.yagl.item.Item;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A basic implementation of {@link Recipe}.
 */
@Getter
@EqualsAndHashCode
@ToString
abstract class RecipeImpl implements Recipe {
    protected final @NotNull String id;
    protected @Nullable Item output;

    /**
     * Instantiates a new Recipe.
     *
     * @param id the id
     */
    RecipeImpl(final @NotNull String id) {
        this.id = id;
    }

    @Override
    public @NotNull Recipe setOutput(final @Nullable Item output) {
        this.output = output == null ? null : output.copy(Item.class);
        return this;
    }

}
