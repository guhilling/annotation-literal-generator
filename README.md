[![Build Annotation Literal Generator](https://github.com/guhilling/annotation-literal-generator/actions/workflows/maven.yml/badge.svg)](https://github.com/guhilling/annotation-literal-generator/actions/workflows/maven.yml)
[![Coverage (Sonar)](https://sonarcloud.io/api/project_badges/measure?project=de.hilling.lang.annotations%3Aliteral-generator&metric=coverage)](https://sonarcloud.io/dashboard?id=de.hilling.lang.annotations%3Aliteral-generator)
[![Status (Sonar)](https://sonarcloud.io/api/project_badges/measure?project=de.hilling.lang.annotations%3Aliteral-generator&metric=alert_status)](https://sonarcloud.io/dashboard?id=de.hilling.lang.annotations%3Aliteral-generator)
[![Maintainability (Sonar)](https://sonarcloud.io/api/project_badges/measure?project=de.hilling.lang.annotations%3Aliteral-generator&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=de.hilling.lang.annotations%3Aliteral-generator)
[![Maven Central](https://img.shields.io/maven-central/v/de.hilling.lang.annotations/literal-generator.svg)](http://search.maven.org/#search|gav|1|g:"de.hilling.lang.annotations"%20AND%20a:"literal-generator")

# Annotation Literal Generator

## Introduction

This library should serve primarily education purposes.
It should nevertheless prove useful and is developed and tested to current standards.

## ~~Problem~~ Challenge

When developing CDI applications or extensions you often have to create instances of
annotations e.g. for firing events.
This is supported by the `AnnotationLiteral` superclass.

Unfortunately you have to write quite some boiler plate code. Typical usage
looks like this:

```java
    public void notify(final EventType testCaseLifecycle, Object description) {
        AnnotationLiteral<TestEvent> event = new TestEventLiteral() {
            @Override
            public EventType value() {
                return testCaseLifecycle;
            }
        };
        lifecycleEvent.select(event).fire(description);
    }

    private abstract static class TestEventLiteral extends AnnotationLiteral<TestEvent> implements TestEvent {
    }

```

That's quite some code where you would just like to create an instance of an annotation.

## Solution

This library provides an annotation processor that generates the annotation literals for
you! It's only necessary to include the library into your java project as follows
(see also the integration-tests project):

```xml
        <dependency>
            <groupId>de.hilling.lang.annotations</groupId>
            <artifactId>processor</artifactId>
            <version>1.0</version>
            <scope>provided</scope>
        </dependency>
```

The processor is only needed for the code generation. To compile the generated code you
also need the cdi-api in any version for the `AnnotationLiteral` class.

Your annotation will look like this:

```java
@GenerateLiteral
@interface AnnotatedAnnotation {

    int value();
}
```

And the generated code becomes:

```java
@Generated("de.hilling.lang.annotations.AnnotationLiteralGenerator")
public class AnnotatedAnnotation__Literal extends AnnotationLiteral<AnnotatedAnnotation> implements AnnotatedAnnotation {
    private final int value;

    public AnnotatedAnnotation__Literal(int value) {
        this.value = value;
    }

    @Override
    public int value() {
        return value;
    }
}
```

## LICENSE

 Copyright 2018 Gunnar Hilling

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
