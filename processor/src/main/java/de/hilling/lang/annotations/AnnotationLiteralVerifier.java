package de.hilling.lang.annotations;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;

/**
 * Verify correct usage of the {@link GenerateLiteral} annotation.
 */
public class AnnotationLiteralVerifier extends LiteralProcessor {

    static final String ERROR_MESSAGE_LITERAL = "wrong use of annotation: must be used on an annotation.";
    static final String ERROR_MESSAGE_LITERAL_FOR = "wrong use of annotation: must be used with annotation value.";
    private static final String GENERATE_FOR_CLASS_NAME = GenerateLiteralFor.class.getCanonicalName();

    private TypeMirror generateLiteralModel;
    private Types typeUtils;

    @Override
    public void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        typeUtils = processingEnv.getTypeUtils();
        Elements elementUtils = processingEnv.getElementUtils();
        generateLiteralModel = elementUtils.getTypeElement(GenerateLiteral.class.getName())
                                           .asType();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        roundEnv.getElementsAnnotatedWith(GenerateLiteral.class)
                .forEach(this::verifyGenerateLiteralNotUsedOnAnnotation);
        roundEnv.getElementsAnnotatedWith(GenerateLiteralFor.class)
                .forEach(this::verifyGenerateLiteralForUsedWithAnnotationValue);
        return false;
    }

    private void verifyGenerateLiteralNotUsedOnAnnotation(Element element) {
        if (element.getKind() != ElementKind.ANNOTATION_TYPE) {
            compilerErrorMessageGenerate(element);
        }
    }

    private void verifyGenerateLiteralForUsedWithAnnotationValue(Element element) {
        AnnotationMirror annotationMirror = element.getAnnotationMirrors()
                                           .stream()
                                           .filter(t -> t.getAnnotationType()
                                                         .toString()
                                                         .equals(GENERATE_FOR_CLASS_NAME))
                                           .findFirst()
                                           .orElseThrow(() -> new RuntimeException("couldn't find target annotation"));
        AnnotationValue value = getAnnotationValue(annotationMirror);
        if(!(value instanceof Annotation)) {
            compilerErrorMessageGenerateFor(element, annotationMirror);
        }
    }

    private AnnotationValue getAnnotationValue(AnnotationMirror annotationMirror) {
        for(Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : annotationMirror.getElementValues().entrySet() ) {
            if(entry.getKey().getSimpleName().toString().equals("value")) {
                return entry.getValue();
            }
        }
        throw new RuntimeException("unable to find annotation parameter");
    }

    private void compilerErrorMessageGenerate(Element element) {
        AnnotationMirror annotation = element.getAnnotationMirrors()
                                             .stream()
                                             .filter(m -> typeUtils.isSameType(m.getAnnotationType(),
                                                     generateLiteralModel))
                                             .findFirst()
                                             .orElseThrow(() -> new RuntimeException("internal compiler error"));
        messager().printMessage(Diagnostic.Kind.ERROR, AnnotationLiteralVerifier.ERROR_MESSAGE_LITERAL, element, annotation);
    }

    private void compilerErrorMessageGenerateFor(Element element, AnnotationMirror annotationMirror) {
        messager().printMessage(Diagnostic.Kind.ERROR, AnnotationLiteralVerifier.ERROR_MESSAGE_LITERAL_FOR, element, annotationMirror);
    }
}
