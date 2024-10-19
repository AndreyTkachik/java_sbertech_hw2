package org.example;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GeneratorBuilder {

    private String latestGeneratorName = null;

    public String getLatestGeneratorName() {
        return latestGeneratorName;
    }

    public String buildCode (Class<?> clazz) {
        StringBuilder code = new StringBuilder();
        code.append("package org.example;\n\n");
        code.append("public class ")
                .append(clazz.getSimpleName())
                .append("JSONGenerator {\n\n");
        latestGeneratorName = clazz.getSimpleName() + "JSONGenerator";
        Method[] methods = clazz.getDeclaredMethods();
        List<Method> methodList = new ArrayList<>();
        for (Method method : methods) {
            if (method.getName().startsWith("get") || method.getName().startsWith("is")) {
                methodList.add(method);
            }
        }
        code.append("\tpublic String generateJSON (")
                .append(clazz.getSimpleName())
                .append(" obj) {\n");
        code.append("""
                    \t\tStringBuilder builder = new StringBuilder();
                    \t\tbuilder.append("{\\n");
                    """);
        for (int indx = 0; indx < methodList.size(); ++indx) {
            methodList.get(indx).setAccessible(true);
            String methodName = null;
            if (methodList.get(indx).getName().startsWith("get")) {
                methodName = methodList.get(indx).getName().replace("get", "");
            } else if (methodList.get(indx).getName().startsWith("is")) {
                methodName = methodList.get(indx).getName().replace("is", "");
            }
            code.append("\t\tbuilder.append(\"\\t\\\"")
                    .append(methodName.toLowerCase())
                    .append("\\\"\")")
                    .append(".append(\": \");\n");
            if (methodList.get(indx).getReturnType().equals(String.class)) {
                stringSerialize(code, methodName, methodList, indx);
            } else if (methodList.get(indx).getReturnType().isArray()) {
                arraySerialize(code, methodName, methodList, indx);
            } else if (Collection.class.isAssignableFrom(methodList.get(indx).getReturnType())) {
                collectionSerialize(code, methodName, methodList, indx);
            } else {
                otherTypesSerialize(code, methodName, methodList, indx);
            }
            if (indx < methodList.size() - 1) {
                code.append("\t\tbuilder.append(\",\");\n");
            }
            code.append("\t\tbuilder.append(\"\\n\");\n");
        }
        code.append("\t\tbuilder.append(\"}\");\n");
        code.append("\t\treturn builder.toString();\n");
        code.append("\t}\n");
        code.append("}\n");
        return code.toString();
    }

    private static void otherTypesSerialize(StringBuilder code, String methodName, List<Method> methodList, int indx) {
        code.append("\t\tbuilder.append(obj.")
                .append(methodList.get(indx).getName())
                .append("());\n");
    }

    private static void collectionSerialize(StringBuilder code, String methodName, List<Method> methodList, int indx) {
        code.append("\t\tvar " + methodName + " = obj.")
                .append(methodList.get(indx).getName())
                .append("();\n");
        code.append("\t\tif ("+ methodName +" != null) {\n");
        code.append("\t\t\tbuilder.append(\"[\");\n");
        code.append("\t\t\tfor (var indx = 0; indx < " + methodName + ".size(); ++indx) {\n");
        code.append("\t\t\t\tif (" + methodName + ".get(indx) != null) {\n");
        code.append("\t\t\t\t\tif (" + methodName + ".get(indx) instanceof String) {\n");
        code.append("\t\t\t\t\t\tbuilder.append(\"\\\"\" + " + methodName + ".get(indx) + \"\\\"\");\n");
        code.append("\t\t\t\t\t} else {\n");
        code.append("\t\t\t\t\t\tbuilder.append(" + methodName + ".get(indx));\n");
        code.append("""
                    \t\t\t\t\t}
                    \t\t\t\t} else {
                    \t\t\t\t\tbuilder.append("null");
                    \t\t\t\t}
                    """);
        code.append("\t\t\t\tif (indx < " + methodName + ".size() - 1) {\n");
        code.append("""
                    \t\t\t\t\tbuilder.append(",");
                    \t\t\t\t}
                    \t\t\t}
                    \t\t\tbuilder.append("]");
                    \t\t} else {
                    \t\t\tbuilder.append("null");
                    \t\t}
                    """);
    }

    private static void arraySerialize(StringBuilder code, String methodName, List<Method> methodList, int indx) {
        code.append("\t\tvar " + methodName + " = obj.")
                .append(methodList.get(indx).getName())
                .append("();\n");
        code.append("\t\tif ("+ methodName +" != null) {\n");
        code.append("\t\t\tbuilder.append(\"[\");\n");
        code.append("\t\t\tfor (var indx = 0; indx < " + methodName + ".length; ++indx) {\n");
        code.append("\t\t\t\tif (" + methodName + "[indx] != null) {\n");
        code.append("\t\t\t\t\tif (" + methodName + "[indx] instanceof String) {\n");
        code.append("\t\t\t\t\t\tbuilder.append(\"\\\"\" + " + methodName + "[indx] + \"\\\"\");\n");
        code.append("\t\t\t\t\t} else {\n");
        code.append("\t\t\t\t\t\tbuilder.append(" + methodName + "[indx]);\n");
        code.append("""
                    \t\t\t\t\t}
                    \t\t\t\t} else {
                    \t\t\t\t\tbuilder.append("null");
                    \t\t\t\t}
                    """);
        code.append("\t\t\t\tif (indx < " + methodName + ".length - 1) {\n");
        code.append("""
                    \t\t\t\t\tbuilder.append(",");
                    \t\t\t\t}
                    \t\t\t}
                    \t\t\tbuilder.append("]");
                    \t\t} else {
                    \t\t\tbuilder.append("null");
                    \t\t}
                    """);
    }

    private static void stringSerialize(StringBuilder code, String methodName, List<Method> methodList, int indx) {
        code.append("\t\tvar " + methodName + " = obj.")
                .append(methodList.get(indx).getName())
                .append("();\n");
        code.append("\t\tif ("+ methodName +" != null) {\n");
        code.append("\t\t\tbuilder.append(\"\\\"\")")
                .append(".append(obj.")
                .append(methodList.get(indx).getName())
                .append("())")
                .append(".append(\"\\\"\");\n");
        code.append("\t\t} else {\n");
        code.append("\t\t\tbuilder.append(\"null\");\n");
        code.append("\t\t}\n");
    }
}
