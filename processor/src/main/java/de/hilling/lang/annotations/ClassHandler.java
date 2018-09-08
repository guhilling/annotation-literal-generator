package de.hilling.lang.annotations;

import com.github.javaparser.JavaParser;
import com.github.javaparser.javadoc.JavadocBlockTag;
import com.github.javaparser.javadoc.description.JavadocDescription;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import java.util.Optional;

/**
 * Collect information about the given class.
 */
class ClassHandler {
    private final TypeElement type;
    private final ProcessingEnvironment env;
    private final ClassModel classModel;

    /**
     * @param element               the class to check for attributes.
     * @param processingEnvironment environment.
     */
    ClassHandler(TypeElement element, ProcessingEnvironment processingEnvironment) {
        this.classModel = new ClassModel(processingEnvironment);
        this.type = element;
        this.env = processingEnvironment;
    }

    /**
     * Collect information about class attributes.
     */
    ClassModel invoke() {
        type.getEnclosedElements()
            .stream()
            .filter(element -> element.getKind() == ElementKind.METHOD)
            .map(element -> (ExecutableElement) element)
            .forEach(this::collectAccessorInfo);
        return classModel;
    }

    private void collectAccessorInfo(ExecutableElement methodRef) {
        classModel.addAttribute(methodRef.getSimpleName()
                                         .toString(),
                ImmutableMirrorWithDocumentation.builder()
                                                .mirror(methodRef.getReturnType())
                                                .javadoc(extractReturnDescription(methodRef))
                                                .build());
    }

    private Optional<String> extractReturnDescription(ExecutableElement methodRef) {
        final String docComment = env.getElementUtils()
                                     .getDocComment(methodRef);
        if (docComment == null) {
            return Optional.empty();
        }
        return JavaParser.parseJavadoc(docComment)
                         .getBlockTags()
                         .stream()
                         .filter(t -> t.getType() == JavadocBlockTag.Type.RETURN)
                         .findFirst()
                         .map(JavadocBlockTag::getContent)
                         .map(JavadocDescription::toText);
    }
}
