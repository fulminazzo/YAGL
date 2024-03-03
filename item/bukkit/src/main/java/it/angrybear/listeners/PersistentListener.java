package it.angrybear.listeners;

import it.angrybear.items.PersistentItem;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 * A listener for {@link PersistentItem}s.
 */
public class PersistentListener implements Listener {
    private static boolean INITIALIZED = false;

    /**
     * Instantiates a new Persistent listener.
     */
    public PersistentListener() {
        INITIALIZED = true;
    }

    private void findPersistentItem(final @Nullable ItemStack itemStack,
                                    final @NotNull Consumer<PersistentItem> ifPresent) {
        findPersistentItem(itemStack, ifPresent, null);
    }

    private void findPersistentItem(final @Nullable ItemStack itemStack,
                                    final @NotNull Consumer<PersistentItem> ifPresent,
                                    final @Nullable Runnable orElse) {
        if (itemStack != null) {
            PersistentItem persistentItem = PersistentItem.getPersistentItem(itemStack);
            if (persistentItem != null) {
                ifPresent.accept(persistentItem);
                return;
            }
        }
        if (orElse != null) orElse.run();
    }

    /**
     * Checks if the current listener has been initialized at least once.
     *
     * @return the boolean
     */
    public static boolean isInitialized() {
        return INITIALIZED;
    }
}
