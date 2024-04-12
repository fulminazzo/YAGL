package it.angrybear.yagl.parsers;

import it.angrybear.yagl.utils.ParserUtils;
import it.fulminazzo.fulmicollection.interfaces.functions.TriConsumer;
import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.yamlparser.configuration.IConfiguration;
import it.fulminazzo.yamlparser.parsers.CallableYAMLParser;
import org.jetbrains.annotations.NotNull;

/**
 * This parser allows parsing an object using the {@link CallableYAMLParser} methods.
 * However, it forces the saved object to store a <i>type</i> field, that contains the name of the class of the original object.
 * Upon loading, this class is retrieved (and will throw errors for invalid values) to recreate the object.
 *
 * @param <C> the type parameter
 */
public abstract class TypedParser<C> extends CallableYAMLParser<C> {
    protected static final String TYPE_FIELD = "type";

    /**
     * Instantiates a new Typed parser.
     *
     * @param clazz the clazz
     */
    public TypedParser(final @NotNull Class<C> clazz) {
        super(clazz, (c) -> {
            String type = c.getString(TYPE_FIELD);
            if (type == null) throw new IllegalArgumentException(String.format("'%s' cannot be null", TYPE_FIELD));
            Class<? extends C> clz = ParserUtils.typeToClass(clazz, type);
            return new Refl<>(clz, new Object[0]).getObject();
        });
    }

    @SuppressWarnings("unchecked")
    @Override
    protected TriConsumer<IConfiguration, String, C> getDumper() {
        return (c, s, g) -> {
            super.getDumper().accept(c, s, g);
            if (g == null) return;
            c.set(s + "." + TYPE_FIELD, ParserUtils.classToType(getOClass(), (Class<? extends C>) g.getClass()));
        };
    }
}
