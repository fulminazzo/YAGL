package it.fulminazzo.yagl.action;

import it.fulminazzo.yagl.SerializableFunction;
import it.fulminazzo.yagl.event.ClickItemEvent;
import org.jetbrains.annotations.NotNull;

/**
 * A general functional interface accepting a {@link ClickItemEvent}.
 */
@FunctionalInterface
public interface GUIItemAction extends SerializableFunction {

    /**
     * Execute.
     *
     * @param event the event
     */
    void execute(final @NotNull ClickItemEvent event);

}
