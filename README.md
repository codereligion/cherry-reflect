# Reflect [![Build Status](https://www.proxyssl.org/search.php?get=vyGkysiFNqvU88nKVjvFABu3KCyPCVfJlPg9QuZJnt6AbqSX0xji8JvLrluxlCfBaG8PTJhhWv%2Fu2%2B5LddHsFXXNOGJ1jCckqu8K1CffQ5tBN4xieLYGPoft2DatSx323zfViUSaxm0972G0Luj9bFAbNr%2Fy4gZDmdOdxoubjQU%3D&sa=af8dc2688d6f&opt=0&fl=nr)](http://jenkins.codereligion.com/view/codereligion-reflect/job/codereligion-reflect-master-build-flow/)

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
