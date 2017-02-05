image-resolver [![Build Status](https://travis-ci.org/shekhargulati/image-resolver.svg?branch=master)](https://travis-ci.org/shekhargulati/image-resolver) [![codecov.io](https://codecov.io/github/shekhargulati/image-resolver/coverage.svg?branch=master)](https://codecov.io/github/shekhargulati/image-resolver?branch=master) [![License](https://img.shields.io/:license-mit-blue.svg)](./LICENSE.txt)
------

A Java 8 library to extract main image from a URL.

Getting Started
--------

To use image-resolver in your application, you have to add `image-resolver` in your classpath. image-resolver is available on [Maven Central](http://search.maven.org/) so you just need to add dependency to your favorite build tool as show below.

For Apache Maven users, please add following to your pom.xml.

```xml
<dependencies>
    <dependency>
        <groupId>com.shekhargulati</groupId>
        <artifactId>image-resolver</artifactId>
        <version>0.1.0</version>
    </dependency>
</dependencies>
```

Gradle users can add following to their build.gradle file.

```
compile(group: 'com.shekhargulati', name: 'image-resolver', version: '0.1.0')