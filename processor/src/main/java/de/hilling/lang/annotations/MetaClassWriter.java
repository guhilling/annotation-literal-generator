package de.hilling.lang.annotations;

import java.io.IOException;

import javax.annotation.Generated;
import javax.enterprise.util.AnnotationLiteral;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.util.Types;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

/**
 * Create meta model class for given Type.
 */
class MetaClassWriter {

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
    MetaClassWriter(TypeElement annotationType, ClassModel classModel) {
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
        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(literalClassName).addModifiers(Modifier.PUBLIC);
        addAnnotationGenerated(classBuilder);
        final Types typeUtils = classModel.getEnvironment().getTypeUtils();
        final DeclaredType declaredType = typeUtils.getDeclaredType(annotationType);
        classBuilder.addSuperinterface(TypeName.get(declaredType));
        classBuilder.superclass(ParameterizedTypeName.get(ClassName.get(AnnotationLiteral.class), ClassName.get(declaredType)));

        JavaFile javaFile = JavaFile.builder(ClassName.get(annotationType).packageName(), classBuilder.build())
                                    .indent("    ").build();
        javaFile.writeTo(classModel.getEnvironment().getFiler());
    }

    private void addAnnotationGenerated(TypeSpec.Builder classBuilder) {
        final AnnotationSpec.Builder specBuilder = AnnotationSpec.builder(Generated.class);
        specBuilder.addMember("value", "$S", AnnotationLiteralGenerator.class.getCanonicalName());
        classBuilder.addAnnotation(specBuilder.build());
    }

    private FieldSpec createFieldSpec(String attributeName, AttributeInfo info) {
        final ParameterizedTypeName fieldType = declarationTypeName(info);
        return FieldSpec.builder(fieldType, attributeName)
                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL).build();
    }

    private ParameterizedTypeName declarationTypeName(AttributeInfo info) {
        final TypeName attributeTypeName = TypeName.get(info.getType());
        final TypeName classTypeName = TypeName.get(annotationType.asType());
        final Class declaredClass;
        declaredClass = String.class;
        return ParameterizedTypeName.get(ClassName.get(declaredClass), classTypeName, attributeTypeName);
    }
}
