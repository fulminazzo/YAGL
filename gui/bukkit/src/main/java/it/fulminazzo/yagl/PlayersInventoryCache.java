package it.fulminazzo.yagl;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A cache to handle the storing of {@link Player}s inventory contents.
 */
public class PlayersInventoryCache {
    private final @NotNull Map<UUID, ItemStack[]> internalCache;

    /**
     * Instantiates a new Players inventory cache.
     */
    public PlayersInventoryCache() {
        this.internalCache = new LinkedHashMap<>();
    }

    /**
     * Checks if a contents list is stored for the given player.
     *
     * @param player the player
     * @return true if it is
     */
    public boolean areContentsStored(final @NotNull Player player) {
        return areContentsStored(player.getUniqueId());
    }

    /**
     * Checks if a contents list is stored for the given player.
     *
     * @param uuid the player's uuid
     * @return true if it is
     */
    public boolean areContentsStored(final @NotNull UUID uuid) {
        return this.internalCache.containsKey(uuid);
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
        PlayerInventory inventory = player.getInventory();
        for (int i = 0; i < cache.length; i++) inventory.setItem(i, cache[i]);
    }

    /**
     * Clears the player contents from the internal cache.
     *
     * @param player the player
     */
    public void clearPlayerContents(final @NotNull Player player) {
        clearPlayerContents(player.getUniqueId());
    }

    /**
     * Clears the player contents from the internal cache.
     *
     * @param uuid the player's uuid
     */
    public void clearPlayerContents(final @NotNull UUID uuid) {
        this.internalCache.remove(uuid);
    }

    /**
     * Clears the player storage for the given amount of items.
     * It does NOT use Bukkit inventory counting system,
     * meaning that the first slot is NOT the hotbar, but rather the first
     * in the storage.
     *
     * @param player the player
     * @param amount the amount of items to clear
     */
    public void clearPlayerStorage(final @NotNull Player player, final int amount) {
        PlayerInventory inventory = player.getInventory();
        for (int i = 0; i < Math.min(27, amount); i++) inventory.setItem(i + 9, null);
        for (int i = 27; i < amount; i++) inventory.setItem(i - 27, null);
    }

}
