package de.hilling.lang.annotations;

import javax.annotation.processing.Generated;
import javax.enterprise.util.AnnotationLiteral;

@Generated("de.hilling.lang.annotations.AnnotationLiteralGenerator")
public class AnnotatedAnnotation__Literal extends AnnotationLiteral<AnnotatedAnnotation> implements AnnotatedAnnotation {
    private final int value;

    /**
     * @param value The dummy value of this test annotation.
     */
    public AnnotatedAnnotation__Literal(int value) {
        this.value = value;
    }

    @Override
    public int value() {
        return value;
    }
}
