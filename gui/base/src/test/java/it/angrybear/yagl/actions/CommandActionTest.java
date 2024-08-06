package it.angrybear.yagl.actions;

import it.angrybear.yagl.TestUtils;
import it.fulminazzo.fulmicollection.utils.ClassUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.*;

class CommandActionTest {

    private static Object[] commandClasses() {
        final Class<?> clazz = CommandAction.class;
        return ClassUtils.findClassesInPackage(clazz.getPackage().getName()).stream()
                .filter(clazz::isAssignableFrom).filter(c -> !c.equals(clazz))
                .toArray(Object[]::new);
    }

    @ParameterizedTest
    @MethodSource("commandClasses")
    @DisplayName("Command Action Inheritors Should Call Parent execute Method On Execute")
    void testCommandActionImplementations(Class<? extends CommandAction> clazz) throws InvocationTargetException, IllegalAccessException {
        CommandAction action = mock(clazz);
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getParameterCount() == 1) continue;
            if (!method.getName().equals("execute")) continue;

            Class<?>[] paramTypes = method.getParameterTypes();
            Object[] params = Arrays.stream(paramTypes).map(TestUtils::mockParameter).toArray(Object[]::new);

            when(method.invoke(action, params)).thenCallRealMethod();
            doCallRealMethod().when(action).execute(any());
            method.invoke(action, params);
            verify(action).execute(any());
        }
    }

    @Test
    void testCommandActionsEquality() {
        CommandAction c1 = new GUICommand("command");
        CommandAction c2 = new GUICommand("command");
        assertEquals(c1, c2);
    }

    @Test
    void testCommandActionsInequality() {
        CommandAction c1 = new GUICommand("command");
        CommandAction c2 = new GUICommand("new-command");
        assertNotEquals(c1, c2);
    }
}