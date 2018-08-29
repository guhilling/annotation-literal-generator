package de.hilling.lang.annotations.tests;

import de.hilling.lang.annotations.GenerateLiteral;

/**
 * Annotation for demo application.
 */
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

    Class type() default Object.class;
}
