package de.hilling.lang.annotations;

@GenerateLiteral
@interface AnnotatedAnnotation {

    /**
     * @return The dummy value of this test annotation.
     */
    int value();
}
