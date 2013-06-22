# Reflect [![Build Status](https://api.travis-ci.org/codereligion/reflect.png?branch=master)](https://travis-ci.org/codereligion/reflect)

This project provides convenience methods to do Java bean introspection. It's main functionality is to provide a wrapper around the ```java.beans.Introspector```, which can not handle generic classes to well in Java versions below 1.7.

For more details have a look at the [wiki](https://github.com/codereligion/reflect/wiki).

## Requirements
* Java 1.5 or higher
* dependencies see [maven pom](pom.xml)

## Maven ##
```xml
<dependency>
	<groupId>com.codereligion</groupId>
	<artifactId>codereligion-reflect</artifactId>
	<version>0.1.0-SNAPSHOT</version>
</dependency>
```

## Example code
```java
    // check if a class has a default constructor
    boolean hasDefaultConstructor = Reflector.hasDefaultConstructor(SomeDomainObject.class);
    
    // get property descriptors, even for classes with generic members
    Set<PropertyDescriptor> writeables = Reflector.getWriteableProperties(SomeDomainObject.class);
    Set<PropertyDescriptor> readables = Reflector.getReadableProperties(SomeDomainObject.class);
    Set<PropertyDescriptor> writeableAndReables = Reflector.getWriteableAndReadableProperties(SomeDomainObject.class);
	...
```
