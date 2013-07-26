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

import static com.google.common.base.Preconditions.checkNotNull;

import com.codereligion.reflect.internal.Generics;
import com.codereligion.reflect.internal.HasReadMethod;
import com.codereligion.reflect.internal.HasWriteMethod;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.Set;

/**
 * Provides a wrapper around the {@link Introspector} to fix known issues with
 * generics in Java versions below 1.7.
 * 
 * @author Sebastian Gröbler
 * @since 11.08.2012
 */
public final class Reflector {

    private static final String TYPE_MUST_NOT_BE_NULL = "type must not be null.";

    /**
     * No public constructor for this utility class.
     */
    private Reflector() {
        throw new IllegalAccessError("This is an utility class which must not be instantiated.");
    }

    /**
     * Determines whether the given {@code type} has a default constructor.
     * 
     * @param type the {@link Class} to check
     * @return true if the given {@code type} as a zero argument constructor,
     *         false otherwise
     * @throws NullPointerException when the given parameter is {@code null}
     */
    public static boolean hasDefaultConstructor(final Class<?> type) {

        checkNotNull(type, TYPE_MUST_NOT_BE_NULL);

        final Constructor<?>[] constructors = type.getConstructors();
        for (final Constructor<?> constructor : constructors) {

            final boolean hasZeroArguments = constructor.getParameterTypes().length == 0;
            if (hasZeroArguments) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retrieves a {@link Set} of writeable properties of the given {@code type}
     * . This includes all properties which have a public setter.
     * 
     * @param type the {@link Class} to get the writeable properties for
     * @return a {@link Set} of {@link PropertyDescriptor}s
     * @throws NullPointerException when the given {@code type} is {@code null}
     * @throws IllegalArgumentException when the given {@code type} can not be
     *             introspected
     */
    public static Set<PropertyDescriptor> getWriteableProperties(final Class<?> type) {
        checkNotNull(type, TYPE_MUST_NOT_BE_NULL);
        return getProperties(type, HasWriteMethod.INSTANCE);
    }

    /**
     * Retrieves a {@link Set} of readable properties of the given {@code type}.
     * This includes all properties which have a public getter.
     * 
     * @param type the {@link Class} to get the readable properties for
     * @return a {@link Set} of {@link PropertyDescriptor}s
     * @throws NullPointerException when the given {@code type} is {@code null}
     * @throws IllegalArgumentException when the given {@code type} can not be
     *             introspected
     */
    public static Set<PropertyDescriptor> getReadableProperties(final Class<?> type) {
        checkNotNull(type, TYPE_MUST_NOT_BE_NULL);
        return getProperties(type, HasReadMethod.INSTANCE);
    }

    /**
     * Retrieves a {@link Set} of writeable and readable properties of the given
     * {@code type}. This includes all properties which have a public setter.
     * 
     * @param type the {@link Class} to get the writeable and readable
     *            properties for
     * @return a {@link Set} of {@link PropertyDescriptor}s
     * @throws NullPointerException when the given {@code type} is {@code null}
     * @throws IllegalArgumentException when the given {@code type} can not be
     *             introspected
     */
    public static Set<PropertyDescriptor> getWriteableAndReadableProperties(final Class<?> type) {
        checkNotNull(type, TYPE_MUST_NOT_BE_NULL);
        return getProperties(type, Predicates.and(HasReadMethod.INSTANCE, HasWriteMethod.INSTANCE));
    }

    /**
     * Retrieves a {@link Set} properties for the given {@code type} which apply
     * to the given {@code predicate}. This includes all properties which have a
     * public getter and setter.
     * 
     * @param type the {@link Class} to get the readable properties for
     * @param predicate the predicate to which each {@link PropertyDescriptor}
     *            must apply
     * @return a {@link Set} of {@link PropertyDescriptor}s
     * @throws NullPointerException when the given {@code type} is {@code null}
     * @throws IllegalArgumentException when the given {@code type} can not be
     *             introspected
     */
    private static Set<PropertyDescriptor> getProperties(final Class<?> type, final Predicate<? super PropertyDescriptor> predicate) {

        try {
            final BeanInfo beanInfo = Introspector.getBeanInfo(type);
            final PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

            final Set<PropertyDescriptor> matchingProperties = new HashSet<PropertyDescriptor>();

            for (final PropertyDescriptor propertyDescriptor : propertyDescriptors) {

                final PropertyDescriptor genericTypeAware = Generics.getGenericTypeAwarePropertyDescriptor(type, propertyDescriptor);
                if (predicate.apply(genericTypeAware)) {
                    matchingProperties.add(genericTypeAware);
                }
            }

            return matchingProperties;
        } catch (final IntrospectionException e) {
            throw new IllegalArgumentException("The given class " + type.getCanonicalName() + " can not be introspected. Reason: " + e.getMessage() + ".", e);
        }
    }
}
