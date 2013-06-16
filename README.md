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

## Build

### Setup
* Java 1.5 or higher
* Maven 3

### Continuous integration and local testing
[![Build Status](https://api.travis-ci.org/codereligion/reflect.png?branch=master)](https://travis-ci.org/codereligion/reflect)

We use Travis CI to run our builds. CI compiles the code and runs the tests for OpenJDK 6, OpenJDK 7 and Oracle JDK 7.
It will fail on any compiler warnings. In order to debug compiler warnings and errors more efficiently we recommend to install
the above mentioned JDK versions. We also recommend to use [jEnv](http://www.jenv.be/) by Gildas Cuisinier to switch between them easily.

When you run local tests with: ```mvn clean test``` Maven will automatically try to compile the code and run the tests with 
the Java version which is currently set on your system. Additionally it will check if the code is compatible with the above mentioned Java
version by utilizing the maven animal sniffer plugin.