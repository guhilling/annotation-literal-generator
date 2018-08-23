package de.hilling.lang.annotations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;

/**
 * Properties model for class.
 */
public class ClassModel {
    private final ProcessingEnvironment env;
    private final Map<String, AttributeInfo> attributes = new HashMap<>();
    private final List<String>               names      = new ArrayList<>();

    ClassModel(ProcessingEnvironment env) {
        this.env = env;
    }

    AttributeInfo getInfo(String attributeName) {
        if (!attributes.containsKey(attributeName)) {
            attributes.put(attributeName, new AttributeInfo());
            names.add(attributeName);
        }
        return attributes.get(attributeName);
    }

    ProcessingEnvironment getEnvironment() {
        return env;
    }

    /**
     * @return map with all attributes for current class.
     */
    Map<String, AttributeInfo> attributes() {
        return attributes;
    }

    /**
     * @return attribute names in declaration order.
     */
    List<String> names() {
        return names;
    }
}
