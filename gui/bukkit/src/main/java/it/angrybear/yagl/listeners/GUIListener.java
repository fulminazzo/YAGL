package it.angrybear.yagl.listeners;

import it.angrybear.yagl.guis.GUI;
import it.angrybear.yagl.viewers.Viewer;
import lombok.Getter;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class GUIListener implements Listener {
    @Getter
    private static GUIListener instance;

    private final Map<UUID, GUI> openGUIs;

    public GUIListener() {
        instance = this;
        this.openGUIs = new LinkedHashMap<>();
    }

    public static void openGUI(final @NotNull Viewer viewer, final @NotNull GUI gui) {
        GUIListener listener = getInstance();
        if (listener == null)
            throw new IllegalStateException("GUIListener has not been initialized yet");
        listener.openGUIs.put(viewer.getUniqueId(), gui);
    }
}
