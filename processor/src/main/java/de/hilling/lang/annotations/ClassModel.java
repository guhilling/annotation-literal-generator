package de.hilling.lang.annotations;

import javax.annotation.processing.ProcessingEnvironment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Attributes model for annotation processing.
 */
class ClassModel {
    private final ProcessingEnvironment env;
    private final Map<String, MirrorWithDocumentation> types = new HashMap<>();
    private final List<String> names = new ArrayList<>();

    ClassModel(ProcessingEnvironment env) {
        this.env = env;
    }


    MirrorWithDocumentation getMirrorWithDocumentation(String attributeName) {
        return types.get(attributeName);
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

    void addAttribute(String name, MirrorWithDocumentation mirror) {
        names.add(name);
        types.put(name, mirror);
    }

}
