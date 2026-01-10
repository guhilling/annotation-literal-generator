package de.hilling.lang.annotations;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.javadoc.JavadocBlockTag;
import com.github.javaparser.javadoc.description.JavadocDescription;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import java.util.LinkedHashMap;
import java.util.Optional;

/**
 * Collect information about the given class.
 */
class ClassAnalyzer {
    private final TypeElement type;
    private final ProcessingEnvironment env;
    private final ImmutableClassDescription.Builder classDescription;
    private final LinkedHashMap<String, ImmutableMirrorWithDocumentation> mirrors = new LinkedHashMap<>();

    /**
     * @param element               the class to check for attributes.
     * @param processingEnvironment environment.
     */
    ClassAnalyzer(TypeElement element, ProcessingEnvironment processingEnvironment) {
        this.classDescription = ImmutableClassDescription.builder()
                                                         .environment(processingEnvironment);
        this.type = element;
        this.env = processingEnvironment;
    }

    /**
     * Collect information about annotation attributes.
     *
     * @return {@link ClassDescription} for all parameters.
     */
    ClassDescription invoke() {
        type.getEnclosedElements()
            .stream()
            .filter(element -> element.getKind() == ElementKind.METHOD)
            .map(ExecutableElement.class::cast)
            .forEach(this::collectAccessorInfo);
        return classDescription.mirrors(mirrors)
                               .build();
    }

    private void collectAccessorInfo(ExecutableElement methodRef) {
        mirrors.put(methodRef.getSimpleName()
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
        return StaticJavaParser.parseJavadoc(docComment, false)
                               .getBlockTags()
                               .stream()
                               .filter(t -> t.getType() == JavadocBlockTag.Type.RETURN)
                               .findFirst()
                               .map(JavadocBlockTag::getContent)
                               .map(JavadocDescription::toText);
    }
}
