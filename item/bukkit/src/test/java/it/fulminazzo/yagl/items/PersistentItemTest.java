package it.fulminazzo.yagl.items;

import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.fulmicollection.utils.ReflectionUtils;
import it.fulminazzo.jbukkit.BukkitUtils;
import it.fulminazzo.yagl.items.fields.ItemFlag;
import it.fulminazzo.yagl.listeners.PersistentListener;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PersistentItemTest {

    @BeforeEach
    void setUp() {
        BukkitUtils.setupServer();
    }

    @Test
    void testPersistentListenerNotInitialized() {
        new Refl<>(PersistentListener.class).setFieldObject("INITIALIZED", false);
        try (MockedStatic<Logger> ignored = mockStatic(Logger.class)) {
            Logger mockLogger = mock(Logger.class);
            when(Logger.getGlobal()).thenReturn(mockLogger);

            PersistentItem item = PersistentItem.newItem("diamond");
            item.create();

            Field warningMessageField = PersistentItem.class.getDeclaredField("WARNING_MESSAGE");
            String warningMessage = (String) ReflectionUtils.get(warningMessageField, PersistentItem.class).orElseGet(null);
            verify(mockLogger).warning(warningMessage);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testNullInteractAction() {
        PersistentItem persistentItem = PersistentItem.newItem("stone");
        Player player = mock(Player.class);
        ItemStack itemStack = mock(ItemStack.class);
        assertDoesNotThrow(() -> persistentItem.interact(player, itemStack, Action.LEFT_CLICK_AIR));
    }

    @Test
    void testNullClickAction() {
        PersistentItem persistentItem = PersistentItem.newItem("stone");
        Player player = mock(Player.class);
        ItemStack itemStack = mock(ItemStack.class);
        assertDoesNotThrow(() -> persistentItem.click(player, itemStack, ClickType.DOUBLE_CLICK));
    }

    @Test
    void testClearPersistentItem() {
        // Clear previous
        PersistentItem.clearPersistentItems();

        PersistentItem persistentItem = PersistentItem.newItem(Material.STONE);
        ItemStack itemStack = persistentItem.create();
        PersistentItem.clearPersistentItems();

        assertNull(PersistentItem.getPersistentItem(itemStack));
    }

    @Test
    void testGetPersistentItem() {
        BukkitUtils.setupEnchantments();
        PersistentItem expected = PersistentItem.newItem(Material.DIAMOND_SWORD, 1)
                .addLore("Extremely deep lore").setDisplayName("Cool sword")
                .addEnchantment("flame", 3).addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                .setCustomModelData(3);
        ItemStack itemStack = expected.create();

        assertEquals(expected, PersistentItem.getPersistentItem(itemStack));
    }

    @Test
    void testInteract() {
        AtomicBoolean value = new AtomicBoolean(false);
        PersistentItem persistentItem = PersistentItem.newItem().setMaterial("STONE")
                .onInteract((i, p, a) -> value.set(true))
                .onClick((i, p, a) -> value.set(false));
        persistentItem.click(mock(Player.class), persistentItem.create(), ClickType.DOUBLE_CLICK);
        assertFalse(value.get());
        persistentItem.interact(mock(Player.class), persistentItem.create(), Action.LEFT_CLICK_AIR);
        assertTrue(value.get());
    }

    @Test
    void testClick() {
        AtomicBoolean value = new AtomicBoolean(false);
        PersistentItem persistentItem = PersistentItem.newItem().setMaterial("STONE")
                .onClick((i, p, a) -> value.set(true))
                .onInteract((i, p, a) -> value.set(false));
        persistentItem.interact(mock(Player.class), persistentItem.create(), Action.LEFT_CLICK_AIR);
        assertFalse(value.get());
        persistentItem.click(mock(Player.class), persistentItem.create(), ClickType.DOUBLE_CLICK);
        assertTrue(value.get());
    }

    @Test
    void testStaticInitializers() {
        List<PersistentItem> items = new Refl<>(PersistentItem.class).getStaticMethods().stream()
                .filter(m -> m.getReturnType().equals(PersistentItem.class))
                .filter(m -> m.getName().equals("newItem"))
                .map(m -> {
                    Class<?>[] paramTypes = m.getParameterTypes();
                    Object[] params = Arrays.stream(paramTypes)
                            .map(c -> {
                                if (c.equals(String.class)) return "STONE";
                                else if (c.equals(int.class)) return 1;
                                else if (c.equals(Material.class)) return Material.STONE;
                                else return null;
                            }).toArray(Object[]::new);
                    try {
                        return m.invoke(PersistentItem.class, params);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                })
                .map(m -> (PersistentItem) m)
                .peek(m -> {
                    if (m.getMaterial() == null) m.setMaterial("STONE");
                })
                .collect(Collectors.toList());
        for (PersistentItem i1 : items)
            for (PersistentItem i2 : items)
                assertEquals(i1, i2);
    }
}