package it.angrybear.yagl.utils;

import it.fulminazzo.fulmicollection.objects.Refl;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BukkitUtils {

    public static void setupItemFactory() {
        ItemFactory factory = mock(ItemFactory.class);
        when(factory.getItemMeta(any())).thenAnswer(r -> {
            List<Class<?>> extraInterfaces = new LinkedList<>();
            extraInterfaces.add(Damageable.class);
            if (r.getArgument(0).equals(Material.ENCHANTED_BOOK)) extraInterfaces.add(EnchantmentStorageMeta.class);
            return mock(ItemMeta.class, withSettings().extraInterfaces(extraInterfaces.toArray(new Class[0])));
        });
        Server server = mock(Server.class);
        when(server.getItemFactory()).thenReturn(factory);
        new Refl<>(Bukkit.class).setFieldObject("server", server);
    }
}
