image-resolver [![Build Status](https://travis-ci.org/shekhargulati/image-resolver.svg?branch=master)](https://travis-ci.org/shekhargulati/image-resolver) [![codecov.io](https://codecov.io/github/shekhargulati/image-resolver/coverage.svg?branch=master)](https://codecov.io/github/shekhargulati/image-resolver?branch=master) [![License](https://img.shields.io/:license-apache-blue.svg)](./LICENSE)
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
```

## Using the API

It is very easy to use the API.

### Extracting image from URL.

```java
String url = "https://medium.com/the-mission/how-to-get-people-to-like-you-in-5-seconds-or-less-67e64cb91155#.tp52bdm6m";
Optional<String> mainImage = MainImageResolver.resolveMainImage(url);
// Return  Optional("https://cdn-images-1.medium.com/max/1200/1*-yql2CobEo8rGLCZv2gOyw.jpeg")
```

### Extracting image from HTML

```java
Optional<String> mainImage = MainImageResolver.resolveMainImageFromHtml(html)
```

### Extracting image using your HTML fetcher

If you want to use your own way to fetch HTML then you can use.

```java
Optional<String> mainImage = MainImageResolver.resolveMainImage(url, url -> fetchHtml(), ImageResolvers.webpageResolvers);
```

## Inspiration

This library is inspired by Maurice Svay [ImageResolver](https://github.com/mauricesvay/ImageResolver) JavaScript library.
