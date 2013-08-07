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

import java.awt.Image;
import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.EventSetDescriptor;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;

/**
 * Forwards all calls to the delegate.
 * 
 * @author Sebastian Gröbler
 * @since 08.08.2013
 */
public abstract class ForwardingBeanInfo implements BeanInfo {

    protected abstract BeanInfo delegate();

    @Override
    public BeanInfo[] getAdditionalBeanInfo() {
        return delegate().getAdditionalBeanInfo();
    }

    @Override
    public BeanDescriptor getBeanDescriptor() {
        return delegate().getBeanDescriptor();
    }

    @Override
    public int getDefaultEventIndex() {
        return delegate().getDefaultEventIndex();
    }

    @Override
    public int getDefaultPropertyIndex() {
        return delegate().getDefaultPropertyIndex();
    }

    @Override
    public EventSetDescriptor[] getEventSetDescriptors() {
        return delegate().getEventSetDescriptors();
    }

    @Override
    public Image getIcon(int iconKind) {
        return delegate().getIcon(iconKind);
    }

    @Override
    public MethodDescriptor[] getMethodDescriptors() {
        return delegate().getMethodDescriptors();
    }

    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        return delegate().getPropertyDescriptors();
    }

    @Override
    public String toString() {
        return delegate().toString();
    }
}
