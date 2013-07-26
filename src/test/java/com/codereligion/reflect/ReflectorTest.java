package com.codereligion.reflect;

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

import static com.codereligion.matcher.IsNotInstantiatable.isNotInstantiatable;
import static com.codereligion.matcher.PropertyDescriptorMatchers.containsProperty;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isOneOf;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import com.codereligion.bean.ApiUser;
import com.codereligion.bean.ComplexClass;
import com.codereligion.bean.MissingDefaultConstructor;
import com.codereligion.bean.MissingSetterAndMissingGetter;
import com.codereligion.bean.RestApi;
import com.codereligion.bean.TypeMissmatchBetweenReadAndWriteMethods;
import com.codereligion.bean.User;
import java.beans.PropertyDescriptor;
import java.util.Set;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Tests {@link Reflector}.
 * 
 * @author Sebastian Gröbler
 * @since 12.08.2012
 */
public class ReflectorTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void mustNotBeInstantiatable() {
        assertThat(Reflector.class, isNotInstantiatable());
    }

    @Test
    public void getReadablePropertiesMustReturnAllReadableProperties() {
        final Set<PropertyDescriptor> properties = Reflector.getReadableProperties(MissingSetterAndMissingGetter.class);
        assertThat(properties, is(not(empty())));
        assertThat(properties.size(), is(3));

        assertThat(properties, containsProperty("class"));
        assertThat(properties, containsProperty("readableProperty"));
        assertThat(properties, containsProperty("writeableAndReadableProperty"));
    }

    @Test
    public void getWriteablePropertiesMustReturnAllWriteableProperties() {
        final Set<PropertyDescriptor> properties = Reflector.getWriteableProperties(MissingSetterAndMissingGetter.class);
        assertThat(properties, is(not(empty())));
        assertThat(properties.size(), is(2));
        assertThat(properties, containsProperty("writeableProperty"));
        assertThat(properties, containsProperty("writeableAndReadableProperty"));
    }

    @Test
    public void getWriteableAndReadablePropertiesMustReturnAllWriteableAndReadableProperties() {
        final Set<PropertyDescriptor> properties = Reflector.getWriteableAndReadableProperties(MissingSetterAndMissingGetter.class);
        assertThat(properties, is(not(empty())));
        assertThat(properties.size(), is(1));
        assertThat(properties, containsProperty("writeableAndReadableProperty"));
    }

    @Test
    public void hasDefaultConstructorMustThrowNpeWhenGivenNullBeanClass() {

        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("type must not be null.");

        Reflector.hasDefaultConstructor(null);
    }

    @Test
    public void getWriteableAndReadablePropertiesMustThrowIaeWhenGivenNullType() {

        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("type must not be null.");

        Reflector.getWriteableAndReadableProperties(null);
    }

    @Test
    public void getWriteablePropertiesMustThrowIaeWhenGivenNullType() {

        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("type must not be null.");

        Reflector.getWriteableProperties(null);
    }

    @Test
    public void getReadablePropertiesMustThrowIaeWhenGivenNullType() {

        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("type must not be null.");

        Reflector.getReadableProperties(null);
    }

    @Test
    public void getReadablesPropertiesMustThrowIaeWhenGivenNullType() {

        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("type must not be null.");

        Reflector.getReadableProperties(null);
    }

    @Test
    public void getWriteableAndReadablePropertiesMustReturnOnlyWriteableAndReadbaleProperties() {
        final Set<PropertyDescriptor> properties = Reflector.getWriteableAndReadableProperties(ComplexClass.class);

        assertNotNull(properties);
        assertFalse(properties.isEmpty());

        for (final PropertyDescriptor property : properties) {
            assertNotNull(property.getReadMethod());
            assertNotNull(property.getWriteMethod());
        }
    }

    @Test
    public void getWriteablePropertiesMustReturnPropertiesWhichAreWritebale() {
        final Set<PropertyDescriptor> properties = Reflector.getWriteableProperties(ComplexClass.class);

        assertNotNull(properties);
        assertFalse(properties.isEmpty());

        for (final PropertyDescriptor property : properties) {
            assertNotNull(property.getWriteMethod());
        }
    }

    @Test
    public void getReadablePropertiesMustReturnPropertiesWhichAreReadable() {
        final Set<PropertyDescriptor> properties = Reflector.getReadableProperties(ComplexClass.class);

        assertNotNull(properties);
        assertFalse(properties.isEmpty());

        for (final PropertyDescriptor property : properties) {
            assertNotNull(property.getReadMethod());
        }
    }

    @Test
    public void getWriteableAndReadablePropertiesMustNotCauseIntrospectionBugWhenGivenUnboundGenericClass() {
        final Set<PropertyDescriptor> properties = Reflector.getWriteableAndReadableProperties(User.class);

        assertNotNull(properties);
        assertFalse(properties.isEmpty());
        assertThat(properties.size(), is(1));

        final PropertyDescriptor property = properties.iterator().next();
        assertThat(property.getPropertyType(), is((Object) Integer.class));
        assertNotNull(property.getWriteMethod());
        assertNotNull(property.getReadMethod());
    }

    @Test
    public void getWriteablePropertiesMustNotCauseIntrospectionBugWhenGivenUnboundGenericClass() {
        final Set<PropertyDescriptor> properties = Reflector.getWriteableProperties(User.class);

        assertNotNull(properties);
        assertFalse(properties.isEmpty());
        assertThat(properties.size(), is(1));

        final PropertyDescriptor property = properties.iterator().next();
        assertThat(property.getPropertyType(), is((Object) Integer.class));
        assertNotNull(property.getWriteMethod());
    }

    @Test
    public void getReadablePropertiesMustNotCauseIntrospectionBugWhenGivenUnboundGenericClass() {
        final Set<PropertyDescriptor> properties = Reflector.getReadableProperties(User.class);

        assertNotNull(properties);
        assertFalse(properties.isEmpty());
        assertThat(properties.size(), is(2));

        for (final PropertyDescriptor property : properties) {
            assertThat(property.getPropertyType(), isOneOf((Object) Integer.class, (Object) Class.class));
            assertNotNull(property.getReadMethod());
        }
    }

    @Test
    public void getWriteableAndReadablePropertiesMustNotCauseIntrospectionBugWhenGivenBoundGenericClass() {
        final Set<PropertyDescriptor> properties = Reflector.getWriteableAndReadableProperties(RestApi.class);

        assertNotNull(properties);
        assertFalse(properties.isEmpty());
        assertThat(properties.size(), is(1));

        final PropertyDescriptor property = properties.iterator().next();
        assertThat(property.getPropertyType(), is((Object) ApiUser.class));
        assertNotNull(property.getWriteMethod());
    }

    @Test
    public void getWriteablePropertiesMustNotCauseIntrospectionBugWhenGivenBoundGenericClass() {
        final Set<PropertyDescriptor> properties = Reflector.getWriteableProperties(RestApi.class);

        assertNotNull(properties);
        assertFalse(properties.isEmpty());
        assertThat(properties.size(), is(1));

        final PropertyDescriptor property = properties.iterator().next();
        assertThat(property.getPropertyType(), is((Object) ApiUser.class));
        assertNotNull(property.getWriteMethod());
    }

    @Test
    public void getReadablePropertiesMustNotCauseIntrospectionBugWhenGivenBoundGenericClass() {
        final Set<PropertyDescriptor> properties = Reflector.getReadableProperties(RestApi.class);

        assertNotNull(properties);
        assertFalse(properties.isEmpty());
        assertThat(properties.size(), is(2));

        for (final PropertyDescriptor property : properties) {
            assertThat(property.getPropertyType(), isOneOf((Object) ApiUser.class, (Object) Class.class));
            assertNotNull(property.getReadMethod());
        }
    }

    @Test
    public void getWriteableAndReadablePropertiesMustThrowIaeWhenGivenClassWithTypeMissmatchBetweenGetterAndSetter() {

        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Could not instrospect property: 'created'. Reason: type mismatch between read and write methods.");

        Reflector.getWriteableAndReadableProperties(TypeMissmatchBetweenReadAndWriteMethods.class);
    }

    @Test
    public void getWriteablePropertiesMustThrowIaeWhenGivenClassWithTypeMissmatchBetweenGetterAndSetter() {

        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Could not instrospect property: 'created'. Reason: type mismatch between read and write methods.");

        Reflector.getWriteableProperties(TypeMissmatchBetweenReadAndWriteMethods.class);
    }

    @Test
    public void getReadablePropertiesMustThrowIaeWhenGivenClassWithTypeMissmatchBetweenGetterAndSetter() {

        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Could not instrospect property: 'created'. Reason: type mismatch between read and write methods.");

        Reflector.getReadableProperties(TypeMissmatchBetweenReadAndWriteMethods.class);
    }

    @Test
    public void givenClassWithoutDefaultConstructorShouldReturnFalseWhenCallingHasDefaultConstructor() {
        assertFalse(Reflector.hasDefaultConstructor(MissingDefaultConstructor.class));
    }

    @Test
    public void givenClassWithDefaultConstructorShouldReturnWhenCallingHasDefaultConstructor() {
        assertTrue(Reflector.hasDefaultConstructor(ComplexClass.class));
    }
}
