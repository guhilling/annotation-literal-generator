package de.hilling.lang.annotations;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * Verify correct usage of the {@link GenerateLiteral} annotation.
 */
public class AnnotationLiteralVerifier
        extends LiteralProcessor {

    static final String ERROR_MESSAGE_LITERAL = "wrong use of annotation: must be used on an annotation.";
    static final String ERROR_MESSAGE_LITERAL_FOR = "wrong use of annotation: must be used with annotation value.";

    private TypeMirror generateLiteralModel;
    private TypeMirror annotationModel;
    private Types typeUtils;
    private Elements elementUtils;

    @Override
    public void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        typeUtils = processingEnv.getTypeUtils();
        elementUtils = processingEnv.getElementUtils();
        generateLiteralModel = typeMirror(GenerateLiteral.class);
        annotationModel = typeMirror(Annotation.class);
    }

    private TypeMirror typeMirror(Class<?> clazz) {
        return elementUtils.getTypeElement(clazz.getName()).asType();
    }

    // tag::implementation[]
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        roundEnv.getElementsAnnotatedWith(GenerateLiteral.class)
                .forEach(this::verifyGenerateLiteralNotUsedOnAnnotation);
        roundEnv.getElementsAnnotatedWith(GenerateLiteralFor.class)
                .forEach(this::verifyGenerateLiteralForUsedWithAnnotationValue);
        return false;
    }
    // end::implementation[]

    private void verifyGenerateLiteralNotUsedOnAnnotation(Element element) {
        if (element.getKind() != ElementKind.ANNOTATION_TYPE) {
            compilerErrorMessageGenerate(element);
        }
    }

    // tag::implementation[]
    private void verifyGenerateLiteralForUsedWithAnnotationValue(Element element) {
        AnnotationMirror annotationMirror = annotationToGenerate(element);
        TypeMirror valueMirror = (TypeMirror) getAnnotationValue(annotationMirror).getValue();
        if (!typeUtils.isAssignable(valueMirror, annotationModel)) {
            messager().printMessage(Diagnostic.Kind.ERROR, AnnotationLiteralVerifier.ERROR_MESSAGE_LITERAL_FOR, element, annotationMirror);
        }
    }
    // end::implementation[]

    private void compilerErrorMessageGenerate(Element element) {
        AnnotationMirror annotation = element.getAnnotationMirrors()
                                             .stream()
                                             .filter(m -> typeUtils.isSameType(m.getAnnotationType(),
                                                     generateLiteralModel))
                                             .findFirst()
                                             .orElseThrow(() -> new RuntimeException("internal compiler error"));
        messager().printMessage(Diagnostic.Kind.ERROR, AnnotationLiteralVerifier.ERROR_MESSAGE_LITERAL, element, annotation);
    }

}
