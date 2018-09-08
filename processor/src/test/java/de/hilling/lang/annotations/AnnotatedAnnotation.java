package de.hilling.lang.annotations;

import org.immutables.value.Value;

@GenerateLiteral
@Value.Immutable
@interface AnnotatedAnnotation {

    /**
     * @return The dummy value of this test annotation.
     */
    int value();
}
