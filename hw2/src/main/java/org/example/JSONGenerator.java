package org.example;

import java.lang.reflect.*;
import java.util.*;

public class JSONGenerator<T> implements Serializer<T> {
    private final StringBuilder builder = new StringBuilder();
    private final List<Method> methodList = new ArrayList<>();
    private final List<String> varsNames = new ArrayList<>();

    public JSONGenerator(Class<T> clazz) {
        Method[] methods = clazz.getDeclaredMethods();
        getMethodsStartWithGetOrIs(methods, methodList);
        if (!methodList.isEmpty()) {
            for (int indx = 0; indx < methodList.size(); ++indx) {
                String methodName = null;
                methodName = getMethodName(methodList, indx, methodName);
                var tempMethodName = methodName.toCharArray();
                tempMethodName[0] = Character.toLowerCase(tempMethodName[0]);
                varsNames.add(new String(tempMethodName));
            }
        }
    }

    private void getMethodsStartWithGetOrIs(Method[] methods, List<Method> methodList) {
        for (Method method : methods) {
            if ((method.getReturnType() != void.class)
                    && (method.getParameterTypes().length == 0)) {
                if ((method.getName().startsWith("get") || method.getName().startsWith("is"))
                        && method.accessFlags().contains(AccessFlag.PUBLIC)) {
                    methodList.add(method);
                }
            }
        }
    }

    private String getMethodName(List<Method> methodList, int indx, String methodName) {
        Method method = methodList.get(indx);
        if (method.getName().startsWith("get")) {
            methodName = method.getName().replace("get", "");
        } else if (method.getName().startsWith("is")) {
            methodName = method.getName().replace("is", "");
        }
        return methodName;
    }

    @Override
    public String serialize (T obj) throws RuntimeException {
        try {
            if (obj == null) {
                return "null";
            }
            if (methodList.isEmpty()) {
                builder.append("{\n}");
                return builder.toString();
            }
            builder.append("{\n");
            for (int indx = 0; indx < methodList.size(); ++indx) {
                Object value = methodList.get(indx).invoke(obj);
                builder.append("\t\"")
                        .append(varsNames.get(indx))
                        .append('\"');
                builder.append(": ");
                serializeValues(value);
                builder.append(',');
                builder.append('\n');
            }
            builder.deleteCharAt(builder.length() - 1);
            builder.append('}');
            return builder.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void serializeValues(Object obj) throws RuntimeException {
        if (obj == null) {
            builder.append("null");
            return;
        }
        Class<?> clazz = obj.getClass();
        if (clazz.isArray()) {
            serializeArray(obj);
        } else if (checkPrimitive(obj)) {
            serializePrimitive(obj);
        } else if (obj instanceof String) {
            serializeString(obj);
        } else if (obj instanceof Collection) {
            serializeCollection(obj);
        }
    }

    private void serializeString(Object obj) {
        builder.append('\"');
        for (Character c : obj.toString().toCharArray()) {
            if (c.equals('\n')) {
                builder.append("\\n");
            } else if (c.equals('\r')) {
                builder.append("\\r");
            } else if (c.equals('\t')) {
                builder.append("\\t");
            } else if (c.equals('\\')) {
                builder.append("\\\"");
            } else if (c.equals('\'')) {
                builder.append("\\'");
            } else {
                builder.append(c);
            }
        }
        builder.append('\"');
    }

    private void serializeCollection(Object obj) throws RuntimeException {
        builder.append('[');
        Iterator<?> it = ((Collection<?>) obj).iterator();
        while (it.hasNext()) {
            serializeValues(it.next());
            if (it.hasNext()) {
                builder.append(',');
            }
        }
        builder.append(']');
    }

    private void serializeArray(Object obj) throws RuntimeException {
        builder.append('[');
        for (int indx = 0; indx < Array.getLength(obj); ++indx) {
            serializeValues(Array.get(obj, indx));
            if (indx < Array.getLength(obj) - 1) {
                builder.append(',');
            }
        }
        builder.append(']');
    }

    private void serializePrimitive(Object obj) {
        builder.append(obj.toString());
    }

    private boolean checkPrimitive(Object obj) {
        return obj instanceof Integer ||
                obj instanceof Double ||
                obj instanceof Boolean ||
                obj instanceof Short ||
                obj instanceof Long ||
                obj instanceof Float ||
                obj instanceof Character ||
                obj instanceof Byte;
    }
}
