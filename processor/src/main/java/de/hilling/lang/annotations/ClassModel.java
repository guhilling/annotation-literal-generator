package de.hilling.lang.annotations;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.type.TypeMirror;
import java.util.*;

/**
 * Attributes model for annotation processing.
 */
class ClassModel {
    private final ProcessingEnvironment         env;
    private final Map<String, TypeMirror>       attributes   = new HashMap<>();
    private final Map<String, Optional<String>> descriptions = new HashMap<>();
    private final List<String>                  names        = new ArrayList<>();

    ClassModel(ProcessingEnvironment env) {
        this.env = env;
    }

    TypeMirror getType(String attributeName) {
        return attributes.get(attributeName);
    }

    ProcessingEnvironment getEnvironment() {
        return env;
    }

    /**
     * @return attribute names in declaration order.
     */
    List<String> names() {
        return names;
    }

    void addAttribute(String name, TypeMirror type, String description) {
        names.add(name);
        attributes.put(name, type);
        descriptions.put(name, Optional.ofNullable(description));
    }

    Optional<String> getJavadoc(String attribute) {
        return descriptions.getOrDefault(attribute, Optional.empty());
    }
}
