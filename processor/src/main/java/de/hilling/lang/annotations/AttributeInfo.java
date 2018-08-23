package de.hilling.lang.annotations;

import javax.lang.model.type.TypeMirror;

/**
 * Information about found attribute.
 */
public class AttributeInfo {
    private TypeMirror type;

    public void setType(TypeMirror type) {
        this.type = type;
    }

    public TypeMirror getType() {
        return type;
    }
}
