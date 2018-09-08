package de.hilling.lang.annotations;

import org.immutables.value.Value;

@GenerateLiteral
@interface AnnotatedAnnotation {

    /**
     * @return The dummy value of this test annotation.
     */
    int value();
}
