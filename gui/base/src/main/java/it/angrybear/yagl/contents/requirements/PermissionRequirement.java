package it.angrybear.yagl.contents.requirements;

import it.angrybear.yagl.viewers.Viewer;
import it.fulminazzo.fulmicollection.objects.FieldEquable;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * A type of requirement that checks if the {@link Viewer} has a certain permission.
 */
@Getter
public class PermissionRequirement extends FieldEquable implements RequirementChecker {
    private final String permission;

    /**
     * Instantiates a new Permission requirement.
     *
     * @param permission the permission
     */
    public PermissionRequirement(final @NotNull String permission) {
        this.permission = permission.toLowerCase();
    }

    @Override
    public boolean test(final @NotNull Viewer viewer) {
        return viewer.hasPermission(this.permission);
    }

    @Override
    public String serialize() {
        return this.permission;
    }
}
