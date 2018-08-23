package de.hilling.lang.annotations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.type.TypeMirror;

/**
 * Properties model for annotation.
 */
class ClassModel {
    private final ProcessingEnvironment   env;
    private final Map<String, TypeMirror> attributes = new HashMap<>();
    private final List<String>            names      = new ArrayList<>();

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

    public void addAttribute(String name, TypeMirror type) {
        names.add(name);
        attributes.put(name, type);
    }
}
