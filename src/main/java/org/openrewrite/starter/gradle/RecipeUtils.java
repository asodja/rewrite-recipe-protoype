package org.openrewrite.starter.gradle;

import org.openrewrite.Tree;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.Space;
import org.openrewrite.marker.Markers;

import static java.util.Collections.emptyList;
import static org.openrewrite.starter.gradle.GradleConstants.INPUT_ANNOTATION_PATTERN;
import static org.openrewrite.starter.gradle.GradleConstants.PROPERTY_PATTERN;

public class RecipeUtils {

    public static boolean isSetter(J.MethodDeclaration method) {
        // TODO check also types
        return method.getName().getSimpleName().startsWith("set");
    }

    public static boolean isVariableForPlainProperty(J.VariableDeclarations variableDeclarations) {
        return variableDeclarations.getType() != null && !variableDeclarations.getType().isAssignableFrom(PROPERTY_PATTERN);
    }

    public static boolean isGetterForPlainProperty(J.MethodDeclaration method) {
        return (method.getName().getSimpleName().startsWith("get") || method.getName().getSimpleName().startsWith("is"))
                && method.getLeadingAnnotations().stream().anyMatch(annotation -> annotation.getAnnotationType().getType().isAssignableFrom(INPUT_ANNOTATION_PATTERN))
                && method.getReturnTypeExpression() != null
                && method.getReturnTypeExpression().getType() != null
                && !method.getReturnTypeExpression().getType().isAssignableFrom(PROPERTY_PATTERN);
    }

    public static String getterToField(J.MethodDeclaration method) {
        String getterName = method.getSimpleName();
        String property = getterName.replace("get", "").replace("is", "");
        return property.substring(0, 1).toLowerCase() + property.substring(1);
    }

    public static String setterToField(J.MethodDeclaration method) {
        String getterName = method.getSimpleName();
        String property = getterName.replace("set", "");
        return property.substring(0, 1).toLowerCase() + property.substring(1);
    }

    public static J.Modifier newModifier(J.Modifier.Type type) {
        return new J.Modifier(
                Tree.randomId(),
                Space.build(" ", emptyList()),
                Markers.EMPTY,
                type,
                emptyList()
        );
    }
}