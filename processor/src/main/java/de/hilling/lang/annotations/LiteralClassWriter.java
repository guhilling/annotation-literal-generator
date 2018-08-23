package de.hilling.lang.annotations;

import java.io.IOException;

import javax.annotation.processing.Generated;
import javax.enterprise.util.AnnotationLiteral;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.util.Types;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

/**
 * Create literal for given annotation.
 */
class LiteralClassWriter {

    private static final String SUFFIX = "__Literal";

    private final TypeElement annotationType;
    private final ClassModel  classModel;
    private final String      literalClassName;

    /**
     * Initialize class with {@link TypeElement} and {@link ClassModel} containing attributes.
     *
     * @param annotationType the bean class.
     * @param classModel     attribute informations about the bean class.
     */
    LiteralClassWriter(TypeElement annotationType, ClassModel classModel) {
        this.annotationType = annotationType;
        this.classModel = classModel;
        literalClassName = annotationType.getSimpleName() + SUFFIX;
    }

    /**
     * Create the annotation literal class.
     *
     * @throws IOException if source file cannot be written.
     */
    void invoke() throws IOException {
        final Types typeUtils = classModel.getEnvironment().getTypeUtils();
        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(literalClassName).addModifiers(Modifier.PUBLIC);

        addAnnotationGenerated(classBuilder);

        final DeclaredType declaredType = typeUtils.getDeclaredType(annotationType);
        classBuilder.addSuperinterface(TypeName.get(declaredType));
        classBuilder.superclass(ParameterizedTypeName.get(ClassName.get(AnnotationLiteral.class), ClassName.get(declaredType)));

        final MethodSpec.Builder constructorBuilder = MethodSpec.constructorBuilder();
        constructorBuilder.addModifiers(Modifier.PUBLIC);

        classModel.names()
                  .forEach(attribute -> generateAttributeAndMethod(classBuilder, constructorBuilder, attribute));

        if (!classModel.names().isEmpty()) {
            classBuilder.addMethod(constructorBuilder.build());
        }

        writeSource(classBuilder.build());
    }

    private void generateAttributeAndMethod(TypeSpec.Builder classBuilder, MethodSpec.Builder constructorBuilder,
                                            String attribute) {
        final TypeName typeName = TypeName.get(classModel.getType(attribute));

        classBuilder.addField(FieldSpec.builder(typeName, attribute, Modifier.PRIVATE, Modifier.FINAL).build());

        constructorBuilder.addParameter(typeName, attribute);
        constructorBuilder.addStatement("this.$1L = $1L", attribute);

        final MethodSpec.Builder builder = MethodSpec.methodBuilder(attribute);
        builder.addAnnotation(Override.class);
        builder.addModifiers(Modifier.PUBLIC);
        builder.returns(typeName);
        builder.addStatement("return $L", attribute);
        classBuilder.addMethod(builder.build());
    }

    private void writeSource(TypeSpec typeSpec) throws IOException {
        JavaFile javaFile = JavaFile.builder(ClassName.get(annotationType).packageName(), typeSpec)
                                    .indent("    ").build();
        javaFile.writeTo(classModel.getEnvironment().getFiler());
    }

    private void addAnnotationGenerated(TypeSpec.Builder classBuilder) {
        final AnnotationSpec.Builder specBuilder = AnnotationSpec.builder(Generated.class);
        specBuilder.addMember("value", "$S", AnnotationLiteralGenerator.class.getCanonicalName());
        classBuilder.addAnnotation(specBuilder.build());
    }
}
