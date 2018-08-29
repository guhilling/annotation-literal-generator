package de.hilling.lang.annotations;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
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
        roundEnv.getElementsAnnotatedWith(GenerateLiteral.class).forEach(this::generateLiteral);
        if (!roundEnv.processingOver() && round > 20) {
            messager().printMessage(Diagnostic.Kind.ERROR, "possible processing loop detected (21)");
        }
        round++;
        return false;
    }

    private void generateLiteral(Element element) {
        TypeElement typeElement = (TypeElement) element;
        messager().printMessage(Diagnostic.Kind.NOTE, "processing " + element);
        final ClassModel classModel = new ClassHandler(typeElement, processingEnv).invoke();
        writeLiteralClass(typeElement, classModel);
    }

    private void writeLiteralClass(TypeElement element, ClassModel classModel) {
        try {
            new LiteralClassWriter(element, classModel).invoke();
        } catch (IOException e) {
            messager().printMessage(Diagnostic.Kind.ERROR, "Writing metaclass failed", element);
        }
    }

}
