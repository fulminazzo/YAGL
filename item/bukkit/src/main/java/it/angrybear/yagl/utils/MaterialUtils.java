package it.angrybear.yagl.utils;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

/**
 * A collection of utility methods to work with {@link Material}s.
 */
public class MaterialUtils {

    /**
     * Gets material from the given string.
     *
     * @param rawMaterial the raw material
     * @return the material
     */
    public static Material getMaterial(final @NotNull String rawMaterial) {
        return getMaterial(rawMaterial, false);
    }

    /**
     * Gets material from the given string.
     *
     * @param rawMaterial    the raw material
     * @param throwException if true and the material is not found, an {@link IllegalArgumentException} will be thrown.
     * @return the material
     */
    public static Material getMaterial(final @NotNull String rawMaterial, final boolean throwException) {
        for (final Material material : Material.values())
            if (material.name().equalsIgnoreCase(rawMaterial))
                return material;
        if (throwException) throw new IllegalArgumentException(String.format("Could not find Material: '%s'", rawMaterial));
        return null;
    }
}
