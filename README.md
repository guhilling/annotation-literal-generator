[![Build Status](https://travis-ci.org/guhilling/annotation-literal-generator.svg?branch=master)](https://travis-ci.org/guhilling/annotation-literal-generator)
# Annotation Literal Generator

## Introduction

This library should serve primarily education purposes.
It should nevertheless prove useful and is developed and tested to current standards.

## ~~Problem~~ Challenge

When developing CDI applications or extensions You often have to create instances of
annotations e.g. for firing events.
This is supported by the `AnnotationLiteral` superclass.

Unfortunately You have to do create quite some boiler plate code. Typical usage
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

That's quite some code where You would just like to create an instance of an annotation.

## Solution

This library provides an annotation processor that generates the annotation literals for
You! It's only necessary to include the library into you java project as follows
(see also the integration-tests project):

```xml
        <dependency>
            <groupId>de.hilling.lang.annotations</groupId>
            <artifactId>processor</artifactId>
            <version>1.0</version>
            <scope>provided</scope>
        </dependency>
```

The processor is only needed for the code generation. To compile the generated code You
also need the cdi-api in any version for the `AnnotationLiteral` class.

You code will look as this:

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
