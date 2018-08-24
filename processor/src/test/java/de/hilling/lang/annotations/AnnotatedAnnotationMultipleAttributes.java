package de.hilling.lang.annotations;

@GenerateLiteral
@interface AnnotatedAnnotationMultipleAttributes {

    int value();

    String stringValue();

    Class classValue();

}
