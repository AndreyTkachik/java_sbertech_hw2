package org.example;

import java.lang.reflect.*;
import java.util.*;

public class JSONGenerator implements Serializer {

    @Override
    public String serialize(Object obj) throws IllegalAccessException, InvocationTargetException {
        if (obj == null) {
            return "null";
        }
        Class<?> clazz = obj.getClass();
        if (clazz.isArray()) {
            return serializeArray(obj);
        } else if (checkPrimitive(obj)) {
            return serializePrimitive(obj);
        } else if (obj instanceof String) {
            return serializeString(obj);
        } else if (obj instanceof Collection) {
            return serializeCollection(obj);
        }
        return serializeObject(obj);
    }

    @Override
    public String serializeString(Object obj) {
        return "\"" + obj + "\"";
    }

    @Override
    public String serializeCollection(Object obj) throws IllegalAccessException, InvocationTargetException {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        Iterator<?> it = ((Collection<?>) obj).iterator();
        while (it.hasNext()) {
            builder.append(serialize(it.next()));
            if (it.hasNext()) {
                builder.append(",");
            }
        }
        builder.append("]");
        return builder.toString();
    }

    @Override
    public String serializeArray(Object obj) throws IllegalAccessException, InvocationTargetException {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (int indx = 0; indx < Array.getLength(obj); ++indx) {
            builder.append(serialize(Array.get(obj, indx)));
            if (indx < Array.getLength(obj) - 1) {
                builder.append(",");
            }
        }
        builder.append("]");
        return builder.toString();
    }

    @Override
    public String serializePrimitive(Object obj) {
        return obj.toString();
    }

    @Override
    public String serializeObject(Object obj) throws IllegalAccessException, InvocationTargetException {
        Class<?> clazz = obj.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        List<Method> methodList = new ArrayList<>();
        for (Method method : methods) {
            if ((method.getName().startsWith("get") || method.getName().startsWith("is"))
                && method.canAccess(obj)) {
                methodList.add(method);
            }
        }
        if (methodList.isEmpty()) {
            return "{\n}";
        }
        StringBuilder builder = new StringBuilder();
        builder.append("{\n");
        for (int indx = 0; indx < methodList.size(); ++indx) {
            Object methodProduct = methodList.get(indx).invoke(obj);
            String methodName = null;
            if (methodList.get(indx).getName().startsWith("get")) {
                methodName = methodList.get(indx).getName().replace("get", "");
            } else if (methodList.get(indx).getName().startsWith("is")) {
                methodName = methodList.get(indx).getName().replace("is", "");
            }
            builder.append("\t\"")
                    .append(methodName.toLowerCase())
                    .append("\"");
            builder.append(": ");
            builder.append(serialize(methodProduct));
            if (indx < methodList.size() - 1) {
                builder.append(",");
            }
            builder.append("\n");
        }
        builder.append("}");
        return builder.toString();
    }

    @Override
    public boolean checkPrimitive(Object obj) {
        return obj instanceof Integer ||
                obj instanceof Double ||
                obj instanceof Boolean ||
                obj instanceof Short ||
                obj instanceof Long ||
                obj instanceof Float ||
                obj instanceof Character ||
                obj instanceof Byte;
    }


    public String generateJSON (Object obj) throws IllegalAccessException, InvocationTargetException {
        if (obj == null) {
            return "null";
        }
        return serializeObject(obj);
    }
}
