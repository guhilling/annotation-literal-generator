package de.hilling.lang.annotations;

@GenerateLiteral
@interface AnnotationWithBrokenComments {

    /**
     * @return
     */
    int value();

    /**
     * non-standard doc.
     */
    String name();
}
