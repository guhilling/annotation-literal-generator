package de.hilling.lang.annotations;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Base class for verifier and generator classes.
 */
abstract class LiteralProcessor extends AbstractProcessor {
    private static final String GENERATE_FOR_CLASS_NAME = GenerateLiteralFor.class.getCanonicalName();

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return new HashSet<>(Arrays.asList(GenerateLiteral.class.getCanonicalName(),
                GenerateLiteralFor.class.getCanonicalName()));
    }

    Messager messager() {
        return processingEnv.getMessager();
    }

    AnnotationMirror annotationToGenerate(Element element) {
        return element.getAnnotationMirrors()
                      .stream()
                      .filter(t -> t.getAnnotationType()
                                    .toString()
                                    .equals(GENERATE_FOR_CLASS_NAME))
                      .findFirst()
                      .orElseThrow(() -> new RuntimeException("couldn't find target annotation"));
    }

    AnnotationValue getAnnotationValue(AnnotationMirror annotationMirror) {
        return annotationMirror.getElementValues()
                               .entrySet()
                               .stream()
                               .filter(this::filterValueEntry)
                               .map(Map.Entry::getValue)
                               .findFirst()
                               .orElseThrow(() -> new RuntimeException("unable to find annotation parameter"));
    }

    private boolean filterValueEntry(Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry) {
        return entry.getKey()
                    .getSimpleName()
                    .toString()
                    .equals("value");
    }
}
