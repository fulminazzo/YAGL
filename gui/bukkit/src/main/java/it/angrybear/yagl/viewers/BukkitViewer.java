package it.angrybear.yagl.viewers;

import it.angrybear.yagl.WrappersAdapter;
import it.angrybear.yagl.wrappers.Sound;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

/**
 * An implementation of {@link Viewer} for the Bukkit platform.
 */
class BukkitViewer extends Viewer {

    /**
     * Instantiates a new Viewer.
     *
     * @param uniqueId the unique id
     * @param name     the name
     */
    BukkitViewer(final @NotNull UUID uniqueId, final @NotNull String name) {
        super(uniqueId, name);
    }

    @Override
    public void playSound(@NotNull Sound sound) {
        Player player = getPlayer().orElse(null);
        if (player == null) throw new IllegalStateException();
        WrappersAdapter.playCustomSound(player, sound);
    }

    @Override
    public void executeCommand(final @NotNull String command) {
        Player player = getPlayer().orElse(null);
        if (player == null) throw new IllegalStateException();
        Bukkit.dispatchCommand(player, command);
    }

    @Override
    public boolean hasPermission(@NotNull String permission) {
        Player player = getPlayer().orElse(null);
        return player != null && player.hasPermission(permission);
    }

    /**
     * Gets player.
     *
     * @return the player
     */
    public Optional<Player> getPlayer() {
        Player player = Bukkit.getPlayer(this.uniqueId);
        if (player == null) return Optional.empty();
        else return Optional.ofNullable(player.isOnline() ? player : null);
    }

    /**
     * New viewer viewer.
     *
     * @param player the player
     * @return the viewer
     */
    public static Viewer newViewer(final @NotNull HumanEntity player) {
        return new BukkitViewer(player.getUniqueId(), player.getName());
    }
}
