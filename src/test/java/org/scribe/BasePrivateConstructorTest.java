package org.scribe;

import static junit.framework.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import org.junit.Test;

/**
 *
 */
public abstract class BasePrivateConstructorTest {

    @Test
    @SuppressWarnings("unchecked")
    public void testConstructorIsPrivate() throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
        Constructor constructor = getClazz().getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        constructor.newInstance();
    }

    protected abstract Class getClazz();
}
