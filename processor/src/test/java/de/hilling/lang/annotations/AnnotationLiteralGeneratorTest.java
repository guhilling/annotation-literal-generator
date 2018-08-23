package de.hilling.lang.annotations;

import static com.google.common.truth.Truth.assertAbout;
import static com.google.common.truth.Truth.assertThat;
import static com.google.testing.compile.CompilationSubject.compilations;
import static com.google.testing.compile.Compiler.javac;

import javax.tools.JavaFileObject;

import org.junit.Before;
import org.junit.Test;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;

public class AnnotationLiteralGeneratorTest {

    private Compiler compiler;


    @Before
    public void setUpCompiler() {
        compiler = javac().withProcessors(new AnnotationLiteralVerifier(), new AnnotationLiteralGenerator());
    }

    @Test
    public void failOnIllegalAnnotation() {
        final JavaFileObject illegalSource = source(IllegallyUsedAnnotation.class);
        Compilation compilation = compiler.compile(illegalSource);

        assertAbout(compilations()).that(compilation).hadErrorContaining(AnnotationLiteralVerifier.ERROR_MESSAGE)
                                   .inFile(illegalSource).onLine(5).atColumn(1);
        assertThat(compilation.status()).isEqualTo(Compilation.Status.FAILURE);
    }

    @Test
    public void compileNoElementAnnotation() {
        final JavaFileObject noElementAnnotation = source(NoElementAnnotation.class);
        Compilation compilation = compiler.compile(noElementAnnotation);

        assertAbout(compilations()).that(compilation).succeededWithoutWarnings();

        assertAbout(compilations()).that(compilation).generatedSourceFile(qualifiedName(NoElementAnnotation__Literal.class))
                                   .hasSourceEquivalentTo(source(NoElementAnnotation__Literal.class));
    }

    private JavaFileObject source(Class<?> clazz) {
        return JavaFileObjects.forResource(qualifiedName(clazz) + ".java");
    }

    private String qualifiedName(Class<?> clazz) {
        return clazz.getCanonicalName().replace('.', '/');
    }
}
