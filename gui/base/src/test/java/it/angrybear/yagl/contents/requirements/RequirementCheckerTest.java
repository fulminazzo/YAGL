package it.angrybear.yagl.contents.requirements;

import it.angrybear.yagl.viewers.Viewer;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class RequirementCheckerTest {

    @Test
    void testPermissionRequirement() {
        final PermissionRequirementChecker checker = new PermissionRequirementChecker("yagl.expected");
        final Viewer viewer = mock(Viewer.class);

        checker.test(viewer);

        verify(viewer).hasPermission(checker.getPermission());
    }
}