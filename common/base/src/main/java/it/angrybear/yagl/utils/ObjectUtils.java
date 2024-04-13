package it.angrybear.yagl.utils;

import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.fulmicollection.utils.ReflectionUtils;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * The type Object utils.
 */
public class ObjectUtils {

    /**
     * Copies the given object to a new one.
     *
     * @param <T> the type of the object to copy from
     * @param t   the object to copy from
     * @return the object
     */
    @SuppressWarnings("unchecked")
    public static <T> T copy(final @NotNull T t) {
        Class<? extends T> clazz = (Class<? extends T>) t.getClass();
        try {
            ReflectionUtils.getConstructor(clazz);
        } catch (Exception e) {
            // Get the most abstract class
            Class<?> c = clazz;
            Class<?>[] interfaces;
            while ((interfaces = c.getInterfaces()).length != 0 && !interfaces[0].equals(Iterable.class))
                c = interfaces[0];
            clazz = ReflectionUtils.getClass(c.getCanonicalName() + "Impl");
        }
        return copy(t, clazz);
    }

    /**
     * Copies the given object to a new one using the provided class.
     * If an interface is provided, it tries to convert it to a non-abstract implementation by appending <i>Impl</i>.
     * If no such class is found, an {@link IllegalArgumentException} is thrown.
     *
     * @param <T>   the type of the object to copy from
     * @param <O>   the type of the returned object
     * @param t     the object to copy from
     * @param clazz the class to copy to
     * @return the object
     */
    public static <T, O extends T> O copy(final @NotNull T t, @NotNull Class<O> clazz) {
        if (clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers()))
            try {
                clazz = ReflectionUtils.getClass(clazz.getCanonicalName() + "Impl");
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(String.format("Could not copy object to abstract class '%s': no '%sImpl' class found",
                        clazz.getCanonicalName(), clazz.getCanonicalName()));
            }

        Refl<O> object = new Refl<>(clazz, new Object[0]);
        for (final Field field : object.getNonStaticFields())
            try {
                Object obj1 = ReflectionUtils.get(field, t);
                object.setFieldObject(field, obj1);
            } catch (IllegalArgumentException ignored) {}

        return object.getObject();
    }

}
