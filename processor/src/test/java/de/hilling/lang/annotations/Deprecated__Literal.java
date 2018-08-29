package de.hilling.lang.annotations;

import javax.annotation.processing.Generated;
import javax.enterprise.util.AnnotationLiteral;

@Generated("de.hilling.lang.annotations.AnnotationLiteralGenerator")
public class Deprecated__Literal extends AnnotationLiteral<Deprecated> implements Deprecated {
    private final String since;

    private final boolean forRemoval;

    /**
     * @param since (documentation missing from annotation.)
     * @param forRemoval (documentation missing from annotation.)
     */
    public Deprecated__Literal(String since, boolean forRemoval) {
        this.since = since;
        this.forRemoval = forRemoval;
    }

    @Override
    public String since() {
        return since;
    }

    @Override
    public boolean forRemoval() {
        return forRemoval;
    }
}
