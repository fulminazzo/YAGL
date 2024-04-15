package it.angrybear.yagl.utils;

import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.fulmicollection.utils.ReflectionUtils;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;

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
            while ((interfaces = c.getInterfaces()).length != 0) {
                Class<?> tmp = interfaces[0];
                if (!tmp.equals(Iterable.class) && !tmp.getSimpleName().startsWith("Abstract"))
                    c = tmp;
                else break;
            }
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
    @SuppressWarnings("unchecked")
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
                if (obj1 instanceof Collection) {
                    Class<?> tmpClass = obj1.getClass();
                    // In the case of creation with Arrays.asList()
                    if (tmpClass.getCanonicalName().equals(Arrays.class.getCanonicalName() + ".ArrayList"))
                        tmpClass = ArrayList.class;
                    Class<Collection<Object>> finalClass = (Class<Collection<Object>>) tmpClass;
                    obj1 = ((Collection<?>) obj1).stream()
                            .collect(Collectors.toCollection(() -> new Refl<>(finalClass, new Object[0]).getObject()));
                } else if (obj1 instanceof Map) {
                    Map<Object, Object> map = new HashMap<>();
                    ((Map<Object, Object>) obj1).putAll(map);
                    obj1 = map;
                } else if (obj1 != null)
                    if (obj1.getClass().isArray()) {
                        Object[] tmp = (Object[]) obj1;
                        Object[] arr = (Object[]) Array.newInstance(obj1.getClass().getComponentType(), tmp.length);
                        System.arraycopy(tmp, 0, arr, 0, arr.length);
                        obj1 = arr;
                    } else
                        try {
                            Method copy = obj1.getClass().getDeclaredMethod("copy");
                            obj1 = ReflectionUtils.setAccessible(copy).invoke(obj1);
                        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException ignored) {}
                object.setFieldObject(field, obj1);
            } catch (IllegalArgumentException ignored) {}

        return object.getObject();
    }

}
