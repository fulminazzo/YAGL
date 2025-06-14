package it.fulminazzo.yagl;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A cache to handle the storing of {@link Player}s inventory contents.
 */
class PlayersInventoryCache {
    private final @NotNull Map<UUID, ItemStack[]> internalCache;

    /**
     * Instantiates a new Players inventory cache.
     */
    public PlayersInventoryCache() {
        this.internalCache = new LinkedHashMap<>();
    }

    /**
     * Stores the given player contents in the current cache.
     * Overwrites any previously saved value.
     *
     * @param player the player
     */
    public void storePlayerContents(final @NotNull Player player) {
        ItemStack[] playerContents = player.getInventory().getStorageContents();
        ItemStack[] cache = new ItemStack[playerContents.length];
        System.arraycopy(playerContents, 0, cache, 0, playerContents.length);
        this.internalCache.put(player.getUniqueId(), cache);
    }

    /**
     * Restores the player contents from the cache.
     * DOES NOT check if the contents are available.
     *
     * @param player the player
     */
    public void restorePlayerContents(final @NotNull Player player) {
        ItemStack[] cache = this.internalCache.get(player.getUniqueId());
        ItemStack[] playerContents = player.getInventory().getStorageContents();
        System.arraycopy(cache, 0, playerContents, 0, playerContents.length);
        this.internalCache.remove(player.getUniqueId());
    }

}
