package it.angrybear.yagl.utils;

import it.fulminazzo.fulmicollection.objects.Refl;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Server;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BukkitUtils {
    private static final List<Recipe> RECIPES = new LinkedList<>();

    public static void setupServer() {
        Server server = mock(Server.class);
        when(server.getRecipe(any())).thenAnswer(r -> {
            NamespacedKey key = r.getArgument(0);
            for (Recipe recipe : RECIPES)
                if (key.equals(new Refl<>(recipe).getFieldObject("key")))
                    return recipe;
            return null;
        });
        when(server.addRecipe(any())).thenAnswer(r -> RECIPES.add(r.getArgument(0)));
        new Refl<>(Bukkit.class).setFieldObject("server", server);
    }

    public static void setupItemFactory() {
        setupServer();
        ItemFactory factory = mock(ItemFactory.class);
        when(factory.getItemMeta(any())).thenAnswer(r -> {
            List<Class<?>> extraInterfaces = new LinkedList<>();
            extraInterfaces.add(Damageable.class);
            if (r.getArgument(0).equals(Material.ENCHANTED_BOOK)) extraInterfaces.add(EnchantmentStorageMeta.class);
            return mock(ItemMeta.class, withSettings().extraInterfaces(extraInterfaces.toArray(new Class[0])));
        });
        when(factory.equals(any(), any())).thenAnswer(r -> {
            ItemMeta m1 = r.getArgument(0);
            ItemMeta m2 = r.getArgument(1);
            return m1 != null && m2 != null && Arrays.equals(m1.getClass().getInterfaces(), m2.getClass().getInterfaces());
        });
        when(Bukkit.getServer().getItemFactory()).thenReturn(factory);
    }

    @Test
    void testItemFactory() {
        setupItemFactory();
        ItemFactory itemFactory = Bukkit.getServer().getItemFactory();
        ItemMeta meta1 = itemFactory.getItemMeta(Material.STONE);
        ItemMeta meta2 = itemFactory.getItemMeta(Material.STONE);
        assertTrue(itemFactory.equals(meta1, meta2));
    }
}
