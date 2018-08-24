package de.hilling.lang.annotations;

import javax.annotation.processing.Generated;
import javax.enterprise.util.AnnotationLiteral;

@Generated("de.hilling.lang.annotations.AnnotationLiteralGenerator")
public class AnnotatedAnnotationMultipleAttributes__Literal extends AnnotationLiteral<AnnotatedAnnotationMultipleAttributes> implements AnnotatedAnnotationMultipleAttributes {
    private final int value;

    private final String stringValue;

    private final Class classValue;

    public AnnotatedAnnotationMultipleAttributes__Literal(int value, String stringValue,
            Class classValue) {
        this.value = value;
        this.stringValue = stringValue;
        this.classValue = classValue;
    }

    @Override
    public int value() {
        return value;
    }

    @Override
    public String stringValue() {
        return stringValue;
    }

    @Override
    public Class classValue() {
        return classValue;
    }
}
