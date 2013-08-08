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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.codereligion.reflect.internal.ForwardingBeanInfo;
import java.beans.BeanInfo;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link ForwardingBeanInfo}.
 * 
 * @author sgroebler
 * @since 08.08.2013
 */
public class ForwardingBeanInfoTest {

    private BeanInfo beanInfo;

    private ForwardingBeanInfo forwardingBeanInfo;
    
    @Before
    public void beforeEachTest() {
        beanInfo = mock(BeanInfo.class);
        forwardingBeanInfo = new ForwardingBeanInfo() {
            
            @Override
            protected BeanInfo delegate() {
                return beanInfo;
            }
        };
    }
    
    @Test
    public void mustDelegateCallsToGetAdditionalBeanInfo() {
        forwardingBeanInfo.getAdditionalBeanInfo();
        verify(beanInfo).getAdditionalBeanInfo();
    }

    @Test
    public void mustDelegateCallsToGetBeanDescriptor() {
        forwardingBeanInfo.getBeanDescriptor();
        verify(beanInfo).getBeanDescriptor();
    }

    @Test
    public void mustDelegateCallsToGetDefaultEventIndex() {
        forwardingBeanInfo.getDefaultEventIndex();
        verify(beanInfo).getDefaultEventIndex();
    }

    @Test
    public void mustDelegateCallsToGetDefaultPropertyIndex() {
        forwardingBeanInfo.getDefaultPropertyIndex();
        verify(beanInfo).getDefaultPropertyIndex();
    }

    @Test
    public void mustDelegateCallsToGetEventSetDescriptors() {
        forwardingBeanInfo.getEventSetDescriptors();
        verify(beanInfo).getEventSetDescriptors();
    }

    @Test
    public void mustDelegateCallsToGetIcon() {
        int iconKind = 0;
        forwardingBeanInfo.getIcon(iconKind);
        verify(beanInfo).getIcon(iconKind);
    }

    @Test
    public void mustDelegateCallsToGetMethodDescriptors() {
        forwardingBeanInfo.getMethodDescriptors();
        verify(beanInfo).getMethodDescriptors();
    }
    
    @Test
    public void mustDelegateCallsToGetPropertyDescriptors() {
        forwardingBeanInfo.getPropertyDescriptors();
        verify(beanInfo).getPropertyDescriptors();
    }
}
