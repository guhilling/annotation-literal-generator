package de.hilling.lang.annotations.tests;

import de.hilling.lang.annotations.GenerateLiteral;

@GenerateLiteral
public @interface IntegrationTestAnnotation {

    String value();

    String firstName() default "Gunnar";

    int age() default 42;

    Class type() default Object.class;
}
