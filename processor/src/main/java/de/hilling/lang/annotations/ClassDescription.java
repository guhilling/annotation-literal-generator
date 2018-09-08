package de.hilling.lang.annotations;

import org.immutables.value.Value;

import javax.annotation.processing.ProcessingEnvironment;
import java.util.LinkedHashMap;

@Value.Immutable
public interface ClassDescription {

    ProcessingEnvironment getEnvironment();

    LinkedHashMap<String, ImmutableMirrorWithDocumentation> getMirrors();

    default MirrorWithDocumentation getMirrorWithDocumentation(String attributeName) {
        return getMirrors().get(attributeName);
    }

}
