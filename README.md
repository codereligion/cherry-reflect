# ![cherry-logo](https://raw.githubusercontent.com/codereligion/cherry/master/small-cherry.png) Reflect [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.codereligion/codereligion-cherry-reflect/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.codereligion/codereligion-cherry-reflect) [![Build Status](https://ssl.webpack.de/secure-jenkins.codereligion.com/buildStatus/icon?job=codereligion-cherry-reflect-master-build-flow)](http://jenkins.codereligion.com/view/codereligion-cherry-reflect/job/codereligion-cherry-reflect-master-build-flow/) [![SonarQube Coverage](https://img.shields.io/sonar/http/sonar.codereligion.com/com.codereligion:codereligion-cherry-reflect/coverage.svg)](http://sonar.codereligion.com/dashboard/index/264)

This library is part of the [cherry project](https://github.com/codereligion/cherry), it provides convenience methods to do Java bean introspection. It's main functionality is to provide a very thin wrapper around the ```java.beans.Introspector```, which can not handle generic classes to well in Java version 1.6.

For more details have a look at the [wiki](https://github.com/codereligion/reflect/wiki).

## Requirements
* Java 1.6
* dependencies see [maven pom](pom.xml)

## Example code
```java
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

```


