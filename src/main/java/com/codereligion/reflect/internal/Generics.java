/**
 * Copyright 2013 www.codereligion.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.codereligion.reflect.internal;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class Generics {

    private static final String SETTER_PREFIX = "set";
    private static final String GETTER_PREFIX = "get";
    private static final String BOOLEAN_GETTER_PREFIX = "is";

    /**
     * This method provides a workaround for the java bug documented here:
     * http://bugs.sun.com/view_bug.do?bug_id=6528714
     * 
     * @param type the {@link Class} to which the given
     *            {@code propertyDescriptor} belongs
     * @param propertyDescriptor the {@link PropertyDescriptor} to potentially
     *            workaround
     * @return either the given {@code propertyDescriptor} or a new one which
     *         reflects the underlying property correctly
     * @throws IllegalArgumentException when instantiation of a new
     *             {@link PropertyDescriptor} failed
     */
    public static PropertyDescriptor getGenericTypeAwarePropertyDescriptor(final Class<?> type, final PropertyDescriptor propertyDescriptor) {
    
        if (!hasBug(propertyDescriptor)) {
            return propertyDescriptor;
        }
    
        // create a setter name from the given getter name
        final String propertyName = propertyDescriptor.getName();
        final Method readMethod = propertyDescriptor.getReadMethod();
        final String readMethodName = readMethod.getName();
        final String writeMethodName = determineWriteMethodNameForReadMethod(readMethod);
    
        // try to find public, non bridged write method matching the name
        final Method potentialWriteMethod = getPublicNonBridgedMethod(writeMethodName, type);
    
        if (potentialWriteMethod == null) {
            // did not find a write method so there is actually none
            return propertyDescriptor;
        }
    
        // almost done, but the damn read method could still be broken
        try {
    
            if (!readMethod.isBridge()) {
                // so now we have the write method and the read method is also
                // not bridged
                return new PropertyDescriptor(propertyName, readMethod, potentialWriteMethod);
            }
    
            // yuck, even the bloody read method was wrong, try to get the right
            // one
            final Method potentialReadMethod = getPublicNonBridgedMethod(readMethodName, type);
    
            if (potentialReadMethod != null) {
                // now we have the write method and the read method, hurrah!!!
                return new PropertyDescriptor(propertyName, potentialReadMethod, potentialWriteMethod);
            }
        } catch (final IntrospectionException e) {
            throw new IllegalArgumentException("Could not instrospect property: '" + propertyName + "'. Reason: " + e.getMessage() + ".", e);
        }
    
        throw new IllegalStateException("PropertyDescriptor for property '" + propertyDescriptor.getName() + "' references "
                + "a bridged readMethod which does not seem to have a public method to bridge to.");
    }
    
    /**
     * Determines whether the given {@code propertyDescriptor} may have the
     * characteristics, which indicate that the bug occurred and further
     * analysis may be necessary.
     * 
     * @param propertyDescriptor the {@link PropertyDescriptor} to check
     * @return true if the bug may have occurred on this property descriptor
     */
    private static boolean hasBug(final PropertyDescriptor propertyDescriptor) {
        final Method writeMethod = propertyDescriptor.getWriteMethod();
        final Method readMethod = propertyDescriptor.getReadMethod();
        return readMethod != null && (writeMethod == null || writeMethod.isBridge());
    }

    /**
     * Determines write method name for the given {@code readMethod}.
     * 
     * @param readMethod the {@link Method} to get the write method name for
     * @return the name of the write method
     */
    private static String determineWriteMethodNameForReadMethod(final Method readMethod) {
        final String readMethodName = readMethod.getName();
        if (readMethodName.contains(BOOLEAN_GETTER_PREFIX)) {
            return readMethodName.replace(BOOLEAN_GETTER_PREFIX, SETTER_PREFIX);
        }
        return readMethodName.replace(GETTER_PREFIX, SETTER_PREFIX);
    }


    /**
     * Tries to find a public non-bridged method in the given {@code type} which
     * matches the given {@code methodName}.
     * 
     * @param methodName the name of the {@link Method} to find
     * @param type the {@link Class} in which the {@link Method} is expected
     * @return the matching {@link Method} or {@code null} if it could not be
     *         found
     */
    private static Method getPublicNonBridgedMethod(final String methodName, final Class<?> type) {
        for (final Method method : type.getMethods()) {
            final boolean isMatchingName = method.getName().equals(methodName);
            final boolean isNotBridge = !method.isBridge();
            final boolean isPublic = Modifier.isPublic(method.getModifiers());

            if (isMatchingName && isNotBridge && isPublic) {
                return method;
            }
        }

        return null;
    }
}
