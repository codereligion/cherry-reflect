# Reflect

Provides convenience methods to work with Java beans and reflection. It's main functionality is to provide a wrapper around the ```java.beans.Introspector```, which can not handle generic classes to well.

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

## Build & deploy

### Setup
* Java 1.6 or higher
* Maven 3

### Run tests locally

```mvn clean test```

### Continuous integration
[![Build Status](https://api.travis-ci.org/codereligion/reflect.png?branch=master)](https://travis-ci.org/codereligion/reflect)

We use Travis CI to run our builds.