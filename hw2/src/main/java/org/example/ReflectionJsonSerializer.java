package org.example;

import java.lang.reflect.*;
import java.util.*;

public class ReflectionJsonSerializer {

    private final StringBuilder builder = new StringBuilder();
    private Map<String, Object> methodsAndValues = new HashMap<>();

    public ReflectionJsonSerializer(Map<String, Object> methodsAndValues) {
        this.methodsAndValues = methodsAndValues;
    }

    public String returnJson() throws RuntimeException {
        if (methodsAndValues.isEmpty()) {
            builder.append("{\n}");
            return builder.toString();
        }
        builder.append("{\n");
        for (Map.Entry<String, Object> entry : methodsAndValues.entrySet()) {
            builder.append("\t\"")
                    .append(entry.getKey())
                    .append('\"');
            builder.append(": ");
            serializeValues(entry.getValue());
            builder.append(',');
            builder.append('\n');
        }
        builder.deleteCharAt(builder.length() - 1);
        builder.append('}');
        return builder.toString();
    }

    private void serializeValues(Object obj) throws RuntimeException {
        if (obj == null) {
            builder.append("null");
            return;
        }
        Class<?> clazz = obj.getClass();
        if (clazz.isArray()) {
            serializeArray(obj);
        } else if (isPrimitive(obj)) {
            serializePrimitive(obj);
        } else if (isStringOrChar(obj)) {
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

    private boolean isPrimitive(Object obj) {
        return obj instanceof Number || obj instanceof Boolean;
    }

    private static boolean isStringOrChar(Object obj) {
        return obj instanceof String || obj instanceof Character;
    }
}
