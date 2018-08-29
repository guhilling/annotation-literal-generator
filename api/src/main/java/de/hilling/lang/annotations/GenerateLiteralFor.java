package de.hilling.lang.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation that can be used on any Java class or interface.
 * <p>
 *     The value must be an annotation an for this given annotation an {@link javax.enterprise.util.AnnotationLiteral}
 *     will be generated. The package for the Literal shall be the package of the annotated class.
 * </p>
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface GenerateLiteralFor {

    Class value();
}
