package de.hilling.lang.annotations;

import javax.annotation.processing.Generated;
import javax.enterprise.util.AnnotationLiteral;

/**
 * Implementation of {@link NoElementAnnotation}.
 */
@Generated("de.hilling.lang.annotations.AnnotationLiteralGenerator")
public class AnnotationWithBrokenComments__Literal extends AnnotationLiteral<AnnotationWithBrokenComments>
implements AnnotationWithBrokenComments {
    private final int value;

    private final String name;

    /**
     * @param value (documentation missing from annotation.)
     * @param name (documentation missing from annotation.)
     */
    public AnnotationWithBrokenComments__Literal(int value, String name) {
        this.value = value;
        this.name = name;
    }

    @Override
    public int value() {
        return value;
    }

    @Override
    public String name() {
        return name;
    }
}
