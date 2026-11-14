package it.fulminazzo.yagl.content.requirement;

import it.fulminazzo.yagl.viewer.Viewer;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

/**
 * A type of requirement that checks if the {@link Viewer} has a certain permission.
 */
@Getter
@EqualsAndHashCode
@ToString
public final class PermissionRequirement implements RequirementChecker {
    private static final long serialVersionUID = -1180552017301576406L;

    private final @NotNull String permission;

    /**
     * Instantiates a new Permission requirement.
     *
     * @param permission the permission
     */
    public PermissionRequirement(final @NotNull String permission) {
        this.permission = permission.toLowerCase(Locale.ROOT);
    }

    @Override
    public boolean test(final @NotNull Viewer viewer) {
        return viewer.hasPermission(this.permission);
    }

    @Override
    public @NotNull String serialize() {
        return this.permission;
    }

}
