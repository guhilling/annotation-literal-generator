package de.hilling.lang.annotations;

import org.immutables.value.Value;

import javax.lang.model.type.TypeMirror;
import java.util.Optional;

/**
 * Representation of type mirror including optional javadoc.
 */
@Value.Immutable
public interface MirrorWithDocumentation {

    TypeMirror getMirror();

    Optional<String> getJavadoc();
}
