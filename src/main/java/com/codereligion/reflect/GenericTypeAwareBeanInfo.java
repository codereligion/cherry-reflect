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
package com.codereligion.reflect;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.codereligion.reflect.internal.ForwardingBeanInfo;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * A decorator for a {@link BeanInfo} implementation that assures that known
 * bugs with generics in Java version 1.6 do not occur when reading properties.
 * 
 * @author Sebastian Gröbler
 * @since 08.08.2013
 */
public final class GenericTypeAwareBeanInfo extends ForwardingBeanInfo {

    private static final String SETTER_PREFIX = "set";
    private static final String GETTER_PREFIX = "get";
    private static final String BOOLEAN_GETTER_PREFIX = "is";
    
    /**
     * Creates a new instance which decorates the given {@code beanInfo} object.
     * 
     * @param beanInfo the {@link BeanInfo} implementation to decorate
     * @return the generic type aware {@link BeanInfo} implementation
     * @throws NullPointerException when the given parameter is {@code null}
     * @throws IllegalArgumentException when the given {@link BeanInfo} is already a {@link GenericTypeAwareBeanInfo}
     */
    public static BeanInfo of(final BeanInfo beanInfo) {
        checkNotNull(beanInfo, "beanInfo must not be null.");
        checkArgument(!(beanInfo instanceof GenericTypeAwareBeanInfo), "the given beanInfo is already of type GenericTypeAwareBeanInfo.");
        
        return new GenericTypeAwareBeanInfo(beanInfo);
    }

    /**
     * The decorated object.
     */
    private final BeanInfo beanInfo;

    /**
     * Creates a new instance which decorates the given {@code beanInfo} object.
     * 
     * @param beanInfo the {@link BeanInfo} implementation to decorate
     */
    private GenericTypeAwareBeanInfo(final BeanInfo beanInfo) {
        this.beanInfo = beanInfo;
    }
    
    @Override
    protected BeanInfo delegate() {
        return beanInfo;
    }

    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        final PropertyDescriptor[] propertyDescriptors = delegate().getPropertyDescriptors();
        final Class<?> type = delegate().getBeanDescriptor().getBeanClass();
        final Set<PropertyDescriptor> matchingProperties = new HashSet<PropertyDescriptor>();

        for (final PropertyDescriptor propertyDescriptor : propertyDescriptors) {

            final PropertyDescriptor genericTypeAware = getGenericTypeAwarePropertyDescriptor(type, propertyDescriptor);
            matchingProperties.add(genericTypeAware);
        }

        return matchingProperties.toArray(new PropertyDescriptor[0]);
    }

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
    private PropertyDescriptor getGenericTypeAwarePropertyDescriptor(final Class<?> type, final PropertyDescriptor propertyDescriptor) {

        if (!hasBugCharacteristics(propertyDescriptor)) {
            return propertyDescriptor;
        }

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

        try {
            // now we know, that we have a read method that is bridged and that
            // we can find the real one
            final Method potentialReadMethod = getPublicNonBridgedMethod(readMethodName, type);
            return new PropertyDescriptor(propertyName, potentialReadMethod, potentialWriteMethod);
        } catch (final IntrospectionException e) {
            throw new IllegalArgumentException("Could not instrospect property: '" + propertyName + "'. Reason: " + e.getMessage() + ".", e);
        }
    }

    /**
     * Determines whether the given {@code propertyDescriptor} may have the
     * characteristics, which indicate that the bug occurred and further
     * analysis may be necessary.
     * 
     * @param propertyDescriptor the {@link PropertyDescriptor} to check
     * @return true if the bug may have occurred on this property descriptor
     */
    private boolean hasBugCharacteristics(final PropertyDescriptor propertyDescriptor) {
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
    private String determineWriteMethodNameForReadMethod(final Method readMethod) {
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
    private Method getPublicNonBridgedMethod(final String methodName, final Class<?> type) {
        for (final Method method : type.getMethods()) {
            final boolean isMatchingName = method.getName().equals(methodName);
            final boolean isNotBridge = !method.isBridge();

            if (isNotBridge && isMatchingName) {
                return method;
            }
        }

        return null;
    }
    
    @Override
    public int hashCode() {
        return beanInfo.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return beanInfo.equals(obj);
    }

}
