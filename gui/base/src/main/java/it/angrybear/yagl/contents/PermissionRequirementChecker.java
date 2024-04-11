package it.angrybear.yagl.contents;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import it.angrybear.yagl.viewers.Viewer;

/**
 * A type of requirement that checks if the {@link Viewer} has a certain permission.
 */
@Getter
public class PermissionRequirementChecker implements RequirementChecker {
    private final String permission;

    /**
     * Instantiates a new Permission requirement.
     *
     * @param permission the permission
     */
    public PermissionRequirementChecker(final @NotNull String permission) {
        this.permission = permission;
    }

    @Override
    public boolean test(final @NotNull Viewer viewer) {
        return viewer.hasPermission(this.permission);
    }

    @Override
    public String serialize() {
        return this.permission;
    }

    /**
     * Equals boolean.
     *
     * @param checker the checker
     * @return the boolean
     */
    public boolean equals(PermissionRequirementChecker checker) {
        return checker != null && this.permission.equalsIgnoreCase(checker.permission);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof PermissionRequirementChecker)
            return equals((PermissionRequirementChecker) o);
        return super.equals(o);
    }
}
