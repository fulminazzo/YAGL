package it.fulminazzo.yagl.content;

import it.fulminazzo.yagl.action.GUIItemAction;
import it.fulminazzo.yagl.content.requirement.RequirementChecker;
import it.fulminazzo.yagl.serializer.DataNode;
import it.fulminazzo.yagl.serializer.SerializationException;
import it.fulminazzo.yagl.serializer.TypedSerializer;
import it.fulminazzo.yagl.wrapper.Sound;
import org.jetbrains.annotations.NotNull;

/**
 * A serializer for {@link GUIContent} implementations.
 *
 * @see GUIContent
 * @see TypedSerializer
 */
public final class GUIContentSerializer extends TypedSerializer<GUIContent> {
    static final @NotNull String PRIORITY_PATH = "priority";
    static final @NotNull String CLICK_SOUND_PATH = "clickSound";
    static final @NotNull String REQUIREMENTS_PATH = "requirements";
    static final @NotNull String CLICK_ACTION_PATH = "clickAction";
    static final @NotNull String VARIABLES_PATH = "variables";

    public GUIContentSerializer() {
        super(GUIContent.class);
        registerSerializer(ItemGUIContent.class, new ItemGUIContentSerializer());
    }

    @Override
    protected void loadCommonData(final @NotNull GUIContent value, final @NotNull DataNode node) throws SerializationException {
        node.get(PRIORITY_PATH, Integer.class).ifPresent(value::setPriority);
        node.get(CLICK_SOUND_PATH, Sound.class).ifPresent(value::setClickSound);
        node.get(REQUIREMENTS_PATH, RequirementChecker.class).ifPresent(value::setViewRequirements);
        node.get(CLICK_ACTION_PATH, GUIItemAction.class).ifPresent(value::onClickItem);
        node.getMap(VARIABLES_PATH, String.class, String.class)
                .ifPresent(v -> v.forEach(value::setVariable));
    }

    @Override
    protected void dumpCommonData(final @NotNull GUIContent value, final @NotNull DataNode output) throws SerializationException {
        output.set(PRIORITY_PATH, value.getPriority());
        output.set(CLICK_SOUND_PATH, value.getClickSound());
        output.set(REQUIREMENTS_PATH, value.getRequirements());
        output.set(CLICK_ACTION_PATH, value.getClickAction());
        output.set(VARIABLES_PATH, value.variables());
    }

}
