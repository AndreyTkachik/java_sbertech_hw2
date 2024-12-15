package org.example;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GeneratorBuilder {

    private static final String TAB = "\t";
    private static final String DUO_TAB = TAB + TAB;
    private static final String TRES_TAB = TAB + TAB + TAB;
    private static final String QUADRO_TAB = TAB + TAB + TAB + TAB;
    private static final String QUINQUIES_TAB = TAB + TAB + TAB + TAB + TAB;
    private static final String SEXTO_TAB = TAB + TAB + TAB + TAB + TAB + TAB;
    private static final String STRING = "\"";
    private static final String NEXT_LINE = "\n";
    private final StringBuilder code = new StringBuilder();

    public String getLatestGeneratorName(Class<?> clazz) {
        return clazz.getSimpleName() + "Generator";
    }

    public String buildCode (Class<?> clazz) {
        initHeader(clazz);
        List<Method> methodList = initMethods(clazz);
        initMethodGenerate(clazz);
        buildSerializer(methodList);
        initFooter();
        return code.toString();
    }

    private void initHeader(Class<?> clazz) {
        code.append(clazz.getPackage().toString()).append(";").append(NEXT_LINE);
        code.append(NEXT_LINE);
        code.append("public class ")
                .append(clazz.getSimpleName())
                .append("Generator {").append(NEXT_LINE).append(NEXT_LINE);
    }

    private static List<Method> initMethods(Class<?> clazz) {
        Method[] methods = clazz.getMethods();
        List<Method> methodList = new ArrayList<>();
        for (Method method : methods) {
            if (isCorrectGetMethod(method)) {
                methodList.add(method);
            }
        }
        return methodList;
    }

    private static boolean isCorrectGetMethod(Method method) {
        return (method.getReturnType() != void.class)
                && (method.getParameterTypes().length == 0)
                && !method.getName().equals("getClass")
                && (method.getName().startsWith("get") || method.getName().startsWith("is"));
    }

    private void initMethodGenerate(Class<?> clazz) {
        code.append(TAB + "public String generate (")
                .append(clazz.getSimpleName())
                .append(" obj) {").append(NEXT_LINE);
        code.append("""
                    \t\tStringBuilder builder = new StringBuilder();
                    \t\tbuilder.append("{\\n");
                    """);
    }

    private void initFooter() {
        code.append(DUO_TAB).append("builder.append(").append(STRING).append("}").append(STRING).append(");").append(NEXT_LINE);
        code.append(DUO_TAB).append("return builder.toString();").append(NEXT_LINE);
        code.append(TAB + "}").append(NEXT_LINE);
        code.append("}").append(NEXT_LINE);
    }

    private void buildSerializer(List<Method> methodList) {
        for (Method method : methodList) {
            String methodName = getChangedMethodName(method);
            code.append(DUO_TAB).append("builder.append(").append(STRING).append("\\t\\").append(STRING).append(methodName)
                    .append("\\" + STRING + STRING + ")").append(".append(").append(STRING).append(": ").append(STRING).append(");").append(NEXT_LINE);
            if (method.getReturnType().equals(String.class)) {
                stringSerialize(methodName, method);
            } else if (method.getReturnType().isArray()) {
                arraySerialize(methodName, method);
            } else if (Collection.class.isAssignableFrom(method.getReturnType())) {
                collectionSerialize(methodName, method);
            } else {
                otherTypesSerialize(method);
            }
            code.append(DUO_TAB).append("builder.append(").append(STRING).append("\\n").append(STRING).append(");").append(NEXT_LINE);
        }
        code.deleteCharAt(code.length() - 1);
    }

    private static String getChangedMethodName(Method method) {
        String methodName = null;
        if (method.getName().startsWith("get")) {
            methodName = method.getName().replace("get", "");
        } else if (method.getName().startsWith("is")) {
            methodName = method.getName().replace("is", "");
        }
        assert methodName != null;
        var tempMethodName = methodName.toCharArray();
        tempMethodName[0] = Character.toLowerCase(tempMethodName[0]);
        methodName = new String(tempMethodName);
        return methodName;
    }

    private void stringSerialize(String methodName, Method method) {
        code.append(DUO_TAB).append("var ").append(methodName).append(" = obj.")
                .append(method.getName())
                .append("();").append(NEXT_LINE);
        code.append(DUO_TAB).append("builder.append(").append(STRING).append("\\").append(STRING).append(STRING).append(");").append(NEXT_LINE);
        code.append(DUO_TAB).append("for (Character c : ").append(methodName).append(".toCharArray()) {").append(NEXT_LINE);
        code.append(TRES_TAB).append("if (c.equals(").append(STRING).append("\\n").append(STRING).append(")) {").append(NEXT_LINE);
        code.append(QUADRO_TAB).append("builder.append(").append(STRING).append("\\").append("\\n").append(STRING).append(");").append(NEXT_LINE);
        code.append(TRES_TAB).append("} else if (c.equals(").append(STRING).append("\\r").append(STRING).append(")) {").append(NEXT_LINE);
        code.append(QUADRO_TAB).append("builder.append(").append(STRING).append("\\").append("\\r").append(STRING).append(");").append(NEXT_LINE);
        code.append(TRES_TAB).append("} else if (c.equals(").append(STRING).append("\\t").append(STRING).append(")) {").append(NEXT_LINE);
        code.append(QUADRO_TAB).append("builder.append(").append(STRING).append("\\").append("\\t").append(STRING).append(");").append(NEXT_LINE);
        code.append(TRES_TAB).append("} else if (c.equals(").append(STRING).append("\\\\").append(STRING).append(")) {").append(NEXT_LINE);
        code.append(QUADRO_TAB).append("builder.append(").append(STRING).append("\\").append("\\\\").append(STRING).append(");").append(NEXT_LINE);
        code.append(TRES_TAB).append("} else if (c.equals(").append(STRING).append("\\").append(STRING).append(STRING).append(")) {").append(NEXT_LINE);
        code.append(QUADRO_TAB).append("builder.append(").append(STRING).append("\\").append("\\").append(STRING).append(STRING).append(");").append(NEXT_LINE);
        code.append(TRES_TAB).append("} else {").append(NEXT_LINE);
        code.append(QUADRO_TAB).append("builder.append(c);").append(NEXT_LINE);
        code.append(TRES_TAB).append("}").append(NEXT_LINE);
        code.append(DUO_TAB).append("}").append(NEXT_LINE);
        code.append(DUO_TAB).append("builder.append(").append(STRING).append("\\").append(STRING).append(STRING).append(");").append(NEXT_LINE);
    }

    private void arraySerialize(String methodName, Method method) {
        code.append(DUO_TAB + "var ")
                .append(methodName)
                .append(" = obj.")
                .append(method.getName())
                .append("();").append(NEXT_LINE);
        getElementByDifferentMethod(methodName, ".length", "[indx]");
        elseAppendNull();
        code.append(QUADRO_TAB).append("if (indx < ")
                .append(methodName)
                .append(".length - 1) {").append(NEXT_LINE);
        elseAppendNullAndClose();
    }

    private void collectionSerialize(String methodName, Method method) {
        code.append(TAB + "\tvar ").append(methodName).append(" = obj.")
                .append(method.getName())
                .append("();" + NEXT_LINE);
        getElementByDifferentMethod(methodName, ".size()", ".get(indx)");
        elseAppendNull();
        code.append(QUADRO_TAB + "if (indx < ").append(methodName).append(".size() - 1) {").append(NEXT_LINE);
        elseAppendNullAndClose();
    }

    private void getElementByDifferentMethod(String methodName, String sizeSpecialize, String getSpecialize) {
        code.append(DUO_TAB + "if (").append(methodName).append(" != null) {").append(NEXT_LINE)
                .append(TRES_TAB).append("builder.append(").append(STRING).append("[").append(STRING).append(");").append(NEXT_LINE)
                .append(TRES_TAB).append("for (var indx = 0; indx < ")
                .append(methodName).append(sizeSpecialize)
                .append("; ++indx) {").append(NEXT_LINE)
                .append(QUADRO_TAB).append("if (")
                .append(methodName).append(getSpecialize)
                .append(" != null) {").append(NEXT_LINE)
                .append(QUINQUIES_TAB).append("if (")
                .append(methodName).append(getSpecialize)
                .append(" instanceof String) {").append(NEXT_LINE)
                .append(SEXTO_TAB).append("builder.append(").append(STRING).append("\\").append(STRING).append("\" + ")
                .append(methodName).append(getSpecialize)
                .append(" + ").append(STRING).append("\\").append(STRING).append(STRING).append(");").append(NEXT_LINE)
                .append(QUINQUIES_TAB).append("} else {").append(NEXT_LINE)
                .append(SEXTO_TAB).append("builder.append(")
                .append(methodName).append(getSpecialize)
                .append(");").append(NEXT_LINE);
    }

    private void elseAppendNullAndClose() {
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

    private void elseAppendNull() {
        code.append("""
                    \t\t\t\t\t}
                    \t\t\t\t} else {
                    \t\t\t\t\tbuilder.append("null");
                    \t\t\t\t}
                    """);
    }

    private void otherTypesSerialize(Method method) {
        code.append(TAB).append("\tbuilder.append(obj.")
                .append(method.getName())
                .append("());").append(NEXT_LINE);
    }
}
