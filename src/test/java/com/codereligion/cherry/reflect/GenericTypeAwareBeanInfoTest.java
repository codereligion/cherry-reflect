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

package com.codereligion.cherry.reflect;

import static org.mockito.Mockito.mock;

import java.beans.BeanInfo;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Tests {@link GenericTypeAwareBeanInfo}.
 * 
 * @author Sebastian Gröbler
 * @since 08.08.2013
 */
public class GenericTypeAwareBeanInfoTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    
    @Test
    public void factoryMustThrowNPEForNullParameter() {
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("beanInfo must not be null.");
        
        GenericTypeAwareBeanInfo.of((BeanInfo) null);
    }
    
    @Test
    public void factoryMustThrowIAEForGenericTypeAwareParameter() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("beanInfo is already of type GenericTypeAwareBeanInfo.");
        
        final BeanInfo beanInfo = GenericTypeAwareBeanInfo.of(mock(BeanInfo.class));
        
        GenericTypeAwareBeanInfo.of(beanInfo);
    }
}
