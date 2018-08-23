package de.hilling.lang.annotations;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

@SupportedAnnotationTypes({"de.hilling.lang.annotations.GenerateLiteral"})
public class AnnotationLiteralVerifier extends AbstractProcessor {

    public static final String ERROR_MESSAGE = "wrong use of annotation: must be used on an annotation.";
    private TypeMirror         generateLiteralModel;
    private Types              typeUtils;

    @Override
    public void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        typeUtils = processingEnv.getTypeUtils();
        Elements elementUtils = processingEnv.getElementUtils();
        generateLiteralModel = elementUtils.getTypeElement(GenerateLiteral.class.getName()).asType();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        roundEnv.getElementsAnnotatedWith(GenerateLiteral.class).forEach(this::verifyNotAnAnnotation);
        return false;
    }

    private void verifyNotAnAnnotation(Element element) {
        if (element.getKind() != ElementKind.ANNOTATION_TYPE) {
            compilerErrorMessage(element);
        }
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    private void compilerErrorMessage(Element element) {
        AnnotationMirror annotation = element.getAnnotationMirrors().stream()
                                             .filter(m -> typeUtils.isSameType(m.getAnnotationType(),
                                                                               generateLiteralModel))
                                             .findFirst()
                                             .orElseThrow(() -> new RuntimeException("internal compiler error"));
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, ERROR_MESSAGE, element, annotation);
    }
}
