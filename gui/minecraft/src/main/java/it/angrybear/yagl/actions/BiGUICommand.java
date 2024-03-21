package it.angrybear.yagl.actions;

import it.angrybear.yagl.guis.GUI;
import it.angrybear.yagl.viewers.Viewer;
import org.jetbrains.annotations.NotNull;

public class BiGUICommand extends CommandAction implements BiGUIAction {

    public BiGUICommand(@NotNull String command) {
        super(command);
    }

    @Override
    public void execute(@NotNull Viewer viewer, @NotNull GUI gui1, @NotNull GUI gui2) {
        super.execute(viewer);
    }
}
