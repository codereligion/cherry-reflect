package com.codereligion.example;

import com.codereligion.example.bean.User;
import com.codereligion.cherry.reflect.BeanIntrospections;
import com.codereligion.cherry.reflect.GenericTypeAwareBeanInfo;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Set;

public class Example {

    public void example() throws IntrospectionException {
        // check if a class has a default constructor
        boolean hasDefaultConstructor = BeanIntrospections.hasDefaultConstructor(User.class);

        // get property descriptors, even for classes with generic members
        Set<PropertyDescriptor> writeables = BeanIntrospections.getWriteableProperties(User.class);
        Set<PropertyDescriptor> readables = BeanIntrospections.getReadableProperties(User.class);
        Set<PropertyDescriptor> writeableAndReables = BeanIntrospections.getWriteableAndReadableProperties(User.class);

        // decorate a BeanInfo so that it can handle generics
        BeanInfo beanInfo = GenericTypeAwareBeanInfo.of(Introspector.getBeanInfo(User.class));
    }
}
