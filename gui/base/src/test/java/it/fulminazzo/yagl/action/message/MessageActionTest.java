package it.fulminazzo.yagl.action.message;

import it.fulminazzo.fulmicollection.utils.ClassUtils;
import it.fulminazzo.yagl.TestUtils;
import it.fulminazzo.yagl.viewer.Viewer;
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

class MessageActionTest {

    private static Object[] messageClasses() {
        final Class<?> clazz = MessageAction.class;
        return ClassUtils.findClassesInPackage(clazz.getPackage().getName()).stream()
                .filter(clazz::isAssignableFrom)
                .filter(c -> !c.equals(clazz))
                .toArray(Object[]::new);
    }

    @ParameterizedTest
    @MethodSource("messageClasses")
    @DisplayName("Message Action Inheritors Should Call sendMessage Method On Viewer")
    void testMessageActionImplementations(Class<? extends MessageAction> clazz) throws InvocationTargetException, IllegalAccessException {
        MessageAction action = mock(clazz);
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getParameterCount() == 1) continue;
            if (!method.getName().equals("execute")) continue;

            Class<?>[] paramTypes = method.getParameterTypes();
            Object[] params = Arrays.stream(paramTypes).map(TestUtils::mockParameter).toArray(Object[]::new);
            Viewer viewer = mock(Viewer.class);
            params[0] = viewer;

            when(method.invoke(action, params)).thenCallRealMethod();
            doCallRealMethod().when(action).sendMessage(any());
            method.invoke(action, params);
            verify(viewer).sendMessage(any());
        }
    }

    @Test
    void testMessageActionsEquality() {
        MessageAction c1 = new GUIMessage("message");
        MessageAction c2 = new GUIMessage("message");
        assertEquals(c1, c2);
    }

    @Test
    void testMessageActionsInequality() {
        MessageAction c1 = new GUIMessage("message");
        MessageAction c2 = new GUIMessage("new-message");
        assertNotEquals(c1, c2);
    }
}