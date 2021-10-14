package de.hilling.lang.annotations;

import com.squareup.javapoet.*;

import javax.annotation.processing.Generated;
import javax.enterprise.util.AnnotationLiteral;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.util.Types;
import java.io.IOException;

import static com.squareup.javapoet.MethodSpec.methodBuilder;

/**
 * Create literal for given annotation.
 */
class LiteralClassWriter {

    private static final String SUFFIX = "__Literal";
    private static final String MISSING_DOC = "@param $L (documentation missing from annotation.)\n";

    private final TypeElement annotationType;
    private final ClassDescription classDescription;
    private final String literalClassName;
    private final PackageElement targetPackage;
    private TypeSpec.Builder classBuilder;
    private MethodSpec.Builder constructorBuilder;

    /**
     * Initialize class with {@link TypeElement} and {@link ClassDescription} containing attributes.
     *
     * @param annotationType the bean class.
     * @param classModel     attribute informations about the bean class.
     */
    LiteralClassWriter(TypeElement annotationType, ClassDescription classModel, PackageElement targetPackage) {
        this.annotationType = annotationType;
        this.classDescription = classModel;
        literalClassName = annotationType.getSimpleName() + SUFFIX;
        this.targetPackage = targetPackage;
    }

    /**
     * Create the annotation literal class.
     *
     * @throws IOException if source file cannot be written.
     */
    void invoke() throws IOException {
        final Types typeUtils = classDescription.getEnvironment()
                                                .getTypeUtils();
        classBuilder = TypeSpec.classBuilder(literalClassName)
                               .addModifiers(Modifier.PUBLIC);

        addAnnotationGenerated();

        final DeclaredType declaredType = typeUtils.getDeclaredType(annotationType);
        classBuilder.addSuperinterface(TypeName.get(declaredType))
                    .superclass(
                            ParameterizedTypeName.get(ClassName.get(AnnotationLiteral.class), TypeName.get(declaredType)))
                    .addJavadoc("Implementation of {@link $T}.\n", annotationType);

        constructorBuilder = MethodSpec.constructorBuilder();
        constructorBuilder.addModifiers(Modifier.PUBLIC);

        classDescription.getMirrors()
                        .keySet()
                        .forEach(this::generateAttributeAndAccessor);

        if (!classDescription.getMirrors()
                             .isEmpty()) {
            classBuilder.addMethod(constructorBuilder.build());
        }

        writeSource(classBuilder.build());
    }

    private void generateAttributeAndAccessor(String attribute) {
        final TypeName typeName = TypeName.get(classDescription.getMirrorWithDocumentation(attribute)
                                                               .getMirror());

        classBuilder.addField(FieldSpec.builder(typeName, attribute, Modifier.PRIVATE, Modifier.FINAL)
                                       .build());

        constructorBuilder.addParameter(typeName, attribute)
                          .addStatement("this.$1L = $1L", attribute);

        classDescription.getMirrorWithDocumentation(attribute)
                        .getJavadoc()
                        .filter(s -> !s.isEmpty())
                        .ifPresentOrElse(s -> constructorBuilder.addJavadoc("@param $L $L\n", attribute, s),
                                () -> constructorBuilder.addJavadoc(MISSING_DOC, attribute));

        classBuilder.addMethod(createAccessor(attribute, typeName));
    }

    private MethodSpec createAccessor(String attribute, TypeName typeName) {
        MethodSpec.Builder builder = methodBuilder(attribute).addAnnotation(Override.class)
                                                             .addModifiers(Modifier.PUBLIC)
                                                             .returns(typeName)
                                                             .addStatement("return $L", attribute);
        return builder.build();
    }

    private void writeSource(TypeSpec typeSpec) throws IOException {
        JavaFile javaFile = JavaFile.builder(targetPackage.toString(), typeSpec)
                                    .indent("    ")
                                    .skipJavaLangImports(true)
                                    .build();
        javaFile.writeTo(classDescription.getEnvironment()
                                         .getFiler());
    }

    private void addAnnotationGenerated() {
        final AnnotationSpec.Builder specBuilder = AnnotationSpec.builder(Generated.class);
        specBuilder.addMember("value", "$S", AnnotationLiteralGenerator.class.getCanonicalName());
        classBuilder.addAnnotation(specBuilder.build());
    }
}
