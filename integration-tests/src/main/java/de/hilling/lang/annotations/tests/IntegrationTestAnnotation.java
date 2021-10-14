package de.hilling.lang.annotations.tests;

import de.hilling.lang.annotations.GenerateLiteral;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Annotation for demo application.
 */
@Target(ElementType.TYPE)
@IntegrationTestAnnotation(value = "Example", type = IntegrationTestAnnotation__Literal.class)
@GenerateLiteral
public @interface IntegrationTestAnnotation {

    /**
     * @return Value of test annotation.
     */
    String value();

    /**
     * @return first name.
     */
    String firstName() default "Gunnar";

    /**
     * @return my age.
     */
    int age() default 42;

    Class<?> type() default Object.class;
}
