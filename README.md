# ![cherry-logo](https://raw.githubusercontent.com/codereligion/cherry/master/small-cherry.png) Reflect [![Build Status](https://ssl.webpack.de/secure-jenkins.codereligion.com/buildStatus/icon?job=codereligion-reflect-master-build-flow)](http://jenkins.codereligion.com/view/codereligion-reflect/job/codereligion-reflect-master-build-flow/)

This library is part of the [cherry project](https://github.com/codereligion/cherry), it provides convenience methods to do Java bean introspection. It's main functionality is to provide a very thin wrapper around the ```java.beans.Introspector```, which can not handle generic classes to well in Java version 1.6.

For more details have a look at the [wiki](https://github.com/codereligion/reflect/wiki).

## Requirements
* Java 1.6
* dependencies see [maven pom](pom.xml)

## Maven ##
```xml
<dependency>
	<groupId>com.codereligion</groupId>
	<artifactId>codereligion-cherry-reflect</artifactId>
	<version>0.2.0-SNAPSHOT</version>
</dependency>
```

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


