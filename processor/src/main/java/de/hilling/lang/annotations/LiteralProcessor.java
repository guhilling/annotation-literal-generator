package de.hilling.lang.annotations;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.lang.model.SourceVersion;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Base class for verifier and generator classes.
 */
abstract class LiteralProcessor extends AbstractProcessor {

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return new HashSet<>(Arrays.asList(GenerateLiteral.class.getCanonicalName(),
                GenerateLiteralFor.class.getCanonicalName()));
    }

    protected Messager messager() {
        return processingEnv.getMessager();
    }

}
