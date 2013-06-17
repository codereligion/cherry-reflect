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

import com.codereligion.reflect.internal.HasReadMethod;
import com.codereligion.reflect.internal.HasWriteMethod;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

/**
 * Reflection utility class.
 * 
 * @author Sebastian Gröbler
 * @since 11.08.2012
 */
public final class Reflector {
	
	private static final String SETTER_PREFIX = "set";
	private static final String GETTER_PREFIX = "get";
	private static final String BOOLEAN_GETTER_PREFIX = "is";

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
	 * @return true if the given {@code type} as a zero argument constructor, false otherwise
	 * @throws IllegalArgumentException when the given parameter is {@code null}
	 */
	public static boolean hasDefaultConstructor(final Class<?> type) {
		
		checkArgument(type != null, "type must not be null.");
		
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
	 * Retrieves a {@link Set} of writeable properties of the given {@code type}.
	 * This includes all properties which have a public setter.
	 * 
	 * @param type the {@link Class} to get the writeable properties for
	 * @return a {@link Set} of {@link PropertyDescriptor}s
	 * @throws IllegalArgumentException when the given {@code type} is {@code null} or can not be introspected
	 */
	public static Set<PropertyDescriptor> getWriteableProperties(final Class<?> type) {
		checkArgument(type != null, "type must not be null.");
		return getProperties(type, HasWriteMethod.INSTANCE);
	}
	
	/**
	 * Retrieves a {@link Set} of readable properties of the given {@code type}.
	 * This includes all properties which have a public getter.
	 * 
	 * @param type the {@link Class} to get the readable properties for
	 * @return a {@link Set} of {@link PropertyDescriptor}s
	 * @throws IllegalArgumentException when the given {@code type} is {@code null} or can not be introspected
	 */
	public static Set<PropertyDescriptor> getReadableProperties(final Class<?> type) {
		checkArgument(type != null, "type must not be null.");
		return getProperties(type, HasReadMethod.INSTANCE);
	}
	
	/**
	 * Retrieves a {@link Set} of writeable and readable properties of the given {@code type}.
	 * This includes all properties which have a public setter.
	 * 
	 * @param type the {@link Class} to get the writeable and readable properties for
	 * @return a {@link Set} of {@link PropertyDescriptor}s
	 * @throws IllegalArgumentException when the given {@code type} is {@code null} or can not be introspected
	 */
	public static Set<PropertyDescriptor> getWriteableAndReadableProperties(final Class<?> type) {
		checkArgument(type != null, "type must not be null.");
		return getProperties(type, Predicates.and(HasReadMethod.INSTANCE, HasWriteMethod.INSTANCE));
	}
	
	/**
	 * Retrieves a {@link Set} properties for the given {@code type} which apply to the given {@code predicate}.
	 * This includes all properties which have a public getter and setter.
	 * 
	 * @param type the {@link Class} to get the readable properties for
	 * @param predicate the predicate to which each {@link PropertyDescriptor} must apply
	 * @return a {@link Set} of {@link PropertyDescriptor}s
	 * @throws IllegalArgumentException when the given {@code type} can not be introspected
	 */
	private static Set<PropertyDescriptor> getProperties(final Class<?> type, final Predicate<? super PropertyDescriptor> predicate) {
		
		try {
			final BeanInfo beanInfo = Introspector.getBeanInfo(type);
		    final PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		    
		    final Set<PropertyDescriptor> settableProperties = new HashSet<PropertyDescriptor>();
		        
		    for (final PropertyDescriptor propertyDescriptor : propertyDescriptors) {
		    	
		    	final PropertyDescriptor genericTypeAware = getGenericTypeAwarePropertyDescriptor(type, propertyDescriptor);
		    	if (predicate.apply(genericTypeAware)) {
		    		settableProperties.add(genericTypeAware);
		    	}
		    }
		    
		    return settableProperties;
		} catch (final IntrospectionException e) {
			throw new IllegalArgumentException("The given class " + type.getCanonicalName() + " can not be introspected. Reason: " + e.getMessage() + ".", e);
		}
	}

	/**
	 * This method provides a workaround for the java bug documented here: http://bugs.sun.com/view_bug.do?bug_id=6528714
	 * 
	 * @param type the {@link Class} to which the given {@code propertyDescriptor} belongs
	 * @param propertyDescriptor the {@link PropertyDescriptor} to potentially workaround
	 * @return either the given {@code propertyDescriptor} or a new one which reflects the underlying property correctly
	 * @throws IllegalArgumentException when instantiation of a new {@link PropertyDescriptor} failed
	 */
	private static PropertyDescriptor getGenericTypeAwarePropertyDescriptor (
			final Class<?> type,
			final PropertyDescriptor propertyDescriptor) {
		
		final Method writeMethod = propertyDescriptor.getWriteMethod();
		final Method readMethod = propertyDescriptor.getReadMethod();
		
		// has a write method and it is not a bridge, so the bug did not occur
		if (writeMethod != null && !writeMethod.isBridge()) {
    		return propertyDescriptor;
		}
		
		// could not find a write method or it was a bridge
		
		// the property actually does not provide a setter
		if (readMethod == null) {
			return propertyDescriptor;
		}
		
		// it has a read method so we can try to find the appropriate write method
		
		// create a setter name from the given getter name
		final String propertyName = propertyDescriptor.getName();
		final String getterName = readMethod.getName();
		final String setterName;
		
		if (getterName.contains(GETTER_PREFIX)) {
			setterName = getterName.replace(GETTER_PREFIX, SETTER_PREFIX);
		} else if (getterName.contains(BOOLEAN_GETTER_PREFIX)) {
			setterName = getterName.replace(BOOLEAN_GETTER_PREFIX, SETTER_PREFIX);
		} else {
			throw new IllegalStateException("Method " + readMethod + " does not comply to java beans conventions.");
		}
		
		// try to find public, non bridged write method matching the name
		final Method potentialWriteMethod = getPublicNonBridgedMethod(setterName, type);
		
		if (potentialWriteMethod == null) {
			// did not find a write method so there is actually none
			return propertyDescriptor;
		}
		
		// almost done, but still the damn read method could still be broken
		
		try {

			if (!readMethod.isBridge()) {
				// so now we have the write method and the read method is also not bridged
				return new PropertyDescriptor(propertyName, readMethod, potentialWriteMethod);
			}
	
			// yuck, even the bloody read method was wrong, try to get the right one
			final Method potentialReadMethod = getPublicNonBridgedMethod(getterName, type);
			
			if (potentialReadMethod != null) {
				// now we have the write method and the read method, hurrah!!! 
				return new PropertyDescriptor(propertyName, potentialReadMethod, potentialWriteMethod);
			}
		} catch (final IntrospectionException e) {
			throw new IllegalArgumentException("Could not instrospect property: '" + propertyName + "'. Reason: " + e.getMessage() + ".", e);
		}

		throw new IllegalStateException(
				"PropertyDescriptor for property '" + propertyDescriptor.getName() + "' references " +
				"a bridged readMethod which does not seem to have a public method to bridge to.");
	}
	
	/**
	 * Tries to find a public non-bridged method in the given {@code type} which matches
	 * the given {@code methodName}.
	 *
	 * @param methodName the name of the {@link Method} to find
	 * @param type the {@link Class} in which the {@link Method} is expected
	 * @return the matching {@link Method} or {@code null} if it could not be found
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
