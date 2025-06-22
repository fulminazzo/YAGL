package it.fulminazzo.yagl.content.requirement;

import it.fulminazzo.yagl.viewer.Viewer;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class RequirementCheckerTest {

    @Test
    void testPermissionRequirement() {
        final PermissionRequirement checker = new PermissionRequirement("yagl.expected");
        final Viewer viewer = mock(Viewer.class);

        checker.test(viewer);

        verify(viewer).hasPermission(checker.getPermission());
    }
}