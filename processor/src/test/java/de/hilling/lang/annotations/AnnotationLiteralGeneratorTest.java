package de.hilling.lang.annotations;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;
import org.junit.Before;
import org.junit.Test;

import javax.tools.JavaFileObject;

import static com.google.common.truth.Truth.assertAbout;
import static com.google.common.truth.Truth.assertThat;
import static com.google.testing.compile.CompilationSubject.compilations;
import static com.google.testing.compile.Compiler.javac;

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

        assertAbout(compilations()).that(compilation).succeeded();

        assertAbout(compilations()).that(compilation).generatedSourceFile(qualifiedName(NoElementAnnotation__Literal.class))
                                   .hasSourceEquivalentTo(source(NoElementAnnotation__Literal.class));
    }

    @Test
    public void compileAnnotatedAnnotation() {
        final JavaFileObject annotatedAnnotation = source(AnnotatedAnnotation.class);
        Compilation compilation = compiler.compile(annotatedAnnotation);

        assertAbout(compilations()).that(compilation).succeeded();

        assertAbout(compilations()).that(compilation).generatedSourceFile(qualifiedName(AnnotatedAnnotation__Literal.class))
                                   .hasSourceEquivalentTo(source(AnnotatedAnnotation__Literal.class));

        assertAbout(compilations()).that(compilation).generatedSourceFile(qualifiedName(AnnotatedAnnotation__Literal.class))
                                   .contentsAsUtf8String().contains("Implementation of {@link AnnotatedAnnotation}.");

        assertAbout(compilations()).that(compilation).generatedSourceFile(qualifiedName(AnnotatedAnnotation__Literal.class))
                                   .contentsAsUtf8String().contains("* @param value The dummy value of this test annotation.");

    }

    @Test
    public void compileAnnotationWithBrokenComments() {
        final JavaFileObject annotatedAnnotation = source(AnnotationWithBrokenComments.class);
        Compilation compilation = compiler.compile(annotatedAnnotation);

        assertAbout(compilations()).that(compilation).succeeded();

        assertAbout(compilations()).that(compilation).generatedSourceFile(qualifiedName(AnnotationWithBrokenComments__Literal.class))
                                   .hasSourceEquivalentTo(source(AnnotationWithBrokenComments__Literal.class));

        assertAbout(compilations()).that(compilation).generatedSourceFile(qualifiedName(AnnotationWithBrokenComments__Literal.class))
                                   .contentsAsUtf8String().contains("* @param value (documentation missing from annotation.)");

        assertAbout(compilations()).that(compilation).generatedSourceFile(qualifiedName(AnnotationWithBrokenComments__Literal.class))
                                   .contentsAsUtf8String().contains("* @param name (documentation missing from annotation.)");

    }

    @Test
    public void compileAnnotatedAnnotationMultipleAttributes() {
        final JavaFileObject annotatedAnnotation = source(AnnotatedAnnotationMultipleAttributes.class);
        Compilation compilation = compiler.compile(annotatedAnnotation);

        assertAbout(compilations()).that(compilation).succeeded();

        assertAbout(compilations()).that(compilation).generatedSourceFile(qualifiedName(AnnotatedAnnotationMultipleAttributes__Literal.class))
                                   .hasSourceEquivalentTo(source(AnnotatedAnnotationMultipleAttributes__Literal.class));
    }

    private JavaFileObject source(Class<?> clazz) {
        return JavaFileObjects.forResource(qualifiedName(clazz) + ".java");
    }

    private String qualifiedName(Class<?> clazz) {
        return clazz.getCanonicalName().replace('.', '/');
    }
}
