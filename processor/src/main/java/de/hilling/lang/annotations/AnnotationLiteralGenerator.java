package de.hilling.lang.annotations;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.SimpleAnnotationValueVisitor9;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.Set;

/**
 * Generate implementations for annotations annotated with {@link GenerateLiteral}.
 */
public class AnnotationLiteralGenerator extends LiteralProcessor {

    private int round;

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        roundEnv.getElementsAnnotatedWith(GenerateLiteral.class)
                .forEach(this::generateLiteral);
        roundEnv.getElementsAnnotatedWith(GenerateLiteralFor.class)
                .forEach(this::generateLiteralFor);
        if (!roundEnv.processingOver() && round > 20) {
            messager().printMessage(Diagnostic.Kind.ERROR, "possible processing loop detected (21)");
        }
        round++;
        return false;
    }

    private void generateLiteral(Element element) {
        TypeElement typeElement = (TypeElement) element;
        messager().printMessage(Diagnostic.Kind.NOTE, "processing " + element);
        final ClassDescription classDescription = new ClassAnalyzer(typeElement, processingEnv).invoke();
        writeLiteralClass(typeElement, classDescription, (PackageElement) element.getEnclosingElement());
    }

    private void generateLiteralFor(Element element) {
        AnnotationMirror annotationMirror = annotationToGenerate(element);
        final AnnotationValue annotationValue = getAnnotationValue(annotationMirror);
        annotationValue.accept(new ImplementationWritingVisitor(element), null);
    }

    private void writeLiteralClass(TypeElement element, ClassDescription classDescription, PackageElement targetPackage) {
        try {
            new LiteralClassWriter(element, classDescription, targetPackage).invoke();
        } catch (IOException e) {
            messager().printMessage(Diagnostic.Kind.ERROR, "Writing metaclass failed", element);
        }
    }

    private class ImplementationWritingVisitor extends SimpleAnnotationValueVisitor9<Void, Void> {
        private final Element element;

        ImplementationWritingVisitor(Element element) {
            this.element = element;
        }

        @Override
        public Void visitType(TypeMirror t, Void p) {
            final TypeElement annotationElement = processingEnv.getElementUtils()
                                                               .getTypeElement(t.toString());
            final ClassDescription description = new ClassAnalyzer(annotationElement, processingEnv).invoke();
            writeLiteralClass(annotationElement, description, (PackageElement) element.getEnclosingElement());
            return p;
        }
    }
}
