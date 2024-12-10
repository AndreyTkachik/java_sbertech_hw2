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

    private String latestGeneratorName = null;

    public String getLatestGeneratorName() {
        return latestGeneratorName;
    }

    public String buildCode (Class<?> clazz) {
        initHeader(clazz);
        Method[] methods = clazz.getDeclaredMethods();
        List<Method> methodList = new ArrayList<>();
        for (Method method : methods) {
            if (method.getName().startsWith("get") || method.getName().startsWith("is")) {
                methodList.add(method);
            }
        }
        initMethodGenerate(clazz);
        buildSerializer(methodList);
        initFooter();
        return code.toString();
    }

    private void buildSerializer(List<Method> methodList) {
        for (int indx = 0; indx < methodList.size(); ++indx) {
            methodList.get(indx).setAccessible(true);
            String methodName = getChangedMethodName(methodList, indx);
            code.append(DUO_TAB + "builder.append(" + STRING + "\\t\\" + STRING)
                    .append(methodName)
                    .append("\\" + STRING + STRING + ")")
                    .append(".append(" + STRING + ": " + STRING + ");" + NEXT_LINE);
            if (methodList.get(indx).getReturnType().equals(String.class)) {
                stringSerialize(methodName, methodList, indx);
            } else if (methodList.get(indx).getReturnType().isArray()) {
                arraySerialize(methodName, methodList, indx);
            } else if (Collection.class.isAssignableFrom(methodList.get(indx).getReturnType())) {
                collectionSerialize(methodName, methodList, indx);
            } else {
                otherTypesSerialize(methodList, indx);
            }
            if (indx < methodList.size() - 1) {
                code.append(DUO_TAB + "builder.append(" + STRING + "," + STRING + ");" + NEXT_LINE);
            }
            code.append(DUO_TAB + "builder.append(" + STRING + "\\n" + STRING + ");" + NEXT_LINE);
        }
    }

    private void collectionSerialize(String methodName, List<Method> methodList, int indx) {
        code.append(TAB + "\tvar " + methodName + " = obj.")
                .append(methodList.get(indx).getName())
                .append("();" + NEXT_LINE);
        getElementByDifferentMethod(methodName, ".size()", ".get(indx)");
        elseAppendNull();
        code.append(QUADRO_TAB + "if (indx < " + methodName + ".size() - 1) {" + NEXT_LINE);
        elseAppendNullAndClose();
    }

    private void arraySerialize(String methodName, List<Method> methodList, int indx) {
        code.append(DUO_TAB + "var " + methodName + " = obj.")
                .append(methodList.get(indx).getName())
                .append("();" + NEXT_LINE);
        getElementByDifferentMethod(methodName, ".length", "[indx]");
        elseAppendNull();
        code.append(TAB + "\t\t\tif (indx < " + methodName + ".length - 1) {" + NEXT_LINE);
        elseAppendNullAndClose();
    }

    private void stringSerialize(String methodName, List<Method> methodList, int indx) {
        code.append(DUO_TAB + "var " + methodName + " = obj.")
                .append(methodList.get(indx).getName())
                .append("();" + NEXT_LINE);
        code.append(DUO_TAB + "if (" + methodName + " != null) {" + NEXT_LINE);
        code.append(TRES_TAB + "builder.append(" + STRING + "\\" + STRING + STRING + ")")
                .append(".append(obj.")
                .append(methodList.get(indx).getName())
                .append("())")
                .append(".append(" + STRING + "\\" + STRING + STRING + ");" + NEXT_LINE);
        code.append(TAB + TAB + "} else {" + NEXT_LINE);
        code.append(TAB + TAB + TAB + "builder.append(" + STRING + "null" + STRING + ");" + NEXT_LINE);
        code.append(TAB + TAB + "}" + NEXT_LINE);
    }

    private static String getChangedMethodName(List<Method> methodList, int indx) {
        String methodName = null;
        Method method = methodList.get(indx);
        if (method.getName().startsWith("get")) {
            methodName = method.getName().replace("get", "");
        } else if (method.getName().startsWith("is")) {
            methodName = method.getName().replace("is", "");
        }
        var tempMethodName = methodName.toCharArray();
        tempMethodName[0] = Character.toLowerCase(tempMethodName[0]);
        methodName = new String(tempMethodName);
        return methodName;
    }

    private void initMethodGenerate(Class<?> clazz) {
        code.append(TAB + "public String generate (")
                .append(clazz.getSimpleName())
                .append(" obj) {" + NEXT_LINE);
        code.append("""
                    \t\tStringBuilder builder = new StringBuilder();
                    \t\tbuilder.append("{\\n");
                    """);
    }

    private void initFooter() {
        code.append(DUO_TAB + "builder.append(" + STRING + "}" + STRING + ");" + NEXT_LINE);
        code.append(DUO_TAB + "return builder.toString();" + NEXT_LINE);
        code.append(TAB + "}" + NEXT_LINE);
        code.append("}" + NEXT_LINE);
    }

    private void initHeader(Class<?> clazz) {
        code.append("package org.clazz;" + NEXT_LINE);
        code.append("import org.example.").append(clazz.getSimpleName()).append(";" + NEXT_LINE);
        code.append(NEXT_LINE);
        code.append("public class ")
                .append(clazz.getSimpleName())
                .append("Generator {" + NEXT_LINE + NEXT_LINE);
        latestGeneratorName = clazz.getSimpleName() + "Generator";
    }

    private void otherTypesSerialize(List<Method> methodList, int indx) {
        code.append(TAB + "\tbuilder.append(obj.")
                .append(methodList.get(indx).getName())
                .append("());" + NEXT_LINE);
    }

    private void getElementByDifferentMethod(String methodName, String sizeSpecialize, String getSpecialize) {
        code.append(DUO_TAB + "if (" + methodName + " != null) {" + NEXT_LINE);
        code.append(TRES_TAB + "builder.append(" + STRING + "[" + STRING + ");" + NEXT_LINE);
        code.append(TRES_TAB + "for (var indx = 0; indx < " + methodName + sizeSpecialize + "; ++indx) {" + NEXT_LINE);
        code.append(QUADRO_TAB + "if (" + methodName + getSpecialize + " != null) {" + NEXT_LINE);
        code.append(QUINQUIES_TAB + "if (" + methodName + getSpecialize + " instanceof String) {" + NEXT_LINE);
        code.append(SEXTO_TAB + "builder.append(" + STRING + "\\" + STRING + "\" + " + methodName + getSpecialize + " + " + STRING + "\\" + STRING + STRING + ");" + NEXT_LINE);
        code.append(QUINQUIES_TAB + "} else {" + NEXT_LINE);
        code.append(SEXTO_TAB + "builder.append(" + methodName + getSpecialize + ");" + NEXT_LINE);
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
}
