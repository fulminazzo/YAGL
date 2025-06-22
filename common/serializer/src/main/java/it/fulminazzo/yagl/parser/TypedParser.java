package it.fulminazzo.yagl.parser;

import it.fulminazzo.yagl.util.ParserUtils;
import it.fulminazzo.fulmicollection.interfaces.functions.TriConsumer;
import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.yamlparser.configuration.ConfigurationSection;
import it.fulminazzo.yamlparser.configuration.IConfiguration;
import it.fulminazzo.yamlparser.parsers.CallableYAMLParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This parser allows parsing an object using the {@link CallableYAMLParser} methods.
 * However, it forces the saved object to store a <i>type</i> field, that contains the class name of the original object.
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
        super(clazz, c -> getObjectFromType(clazz, c));
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

    /**
     * Tries to get the corresponding {@link #TYPE_FIELD} type from the given section, and coverts it to an instance.
     * Throws an {@link IllegalArgumentException} in case no type is provided or no class is found.
     *
     * @param <T>         the type of the object
     * @param objectClass the object class
     * @param section     the section
     * @param parameters  the parameters for the constructor
     * @return the object
     */
    protected static <T> T getObjectFromType(final @NotNull Class<T> objectClass,
                                             final @NotNull ConfigurationSection section,
                                             final Object @Nullable ... parameters) {
        String type = section.getString(TYPE_FIELD);
        if (type == null) throw new IllegalArgumentException(String.format("'%s' cannot be null", TYPE_FIELD));
        Class<? extends T> clazz = ParserUtils.typeToClass(objectClass, type);
        return new Refl<>(clazz, parameters).getObject();
    }
}
