package org.example;

import java.lang.reflect.AccessFlag;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReflectionSerializerFactory<T> implements Serializer<T> {

    private List<Getter> getters = new ArrayList<>();

    public ReflectionSerializerFactory(Class<T> clazz) {
        this.getters = getGetters(clazz);
    }

    private List<Getter> getGetters(Class<?> clazz) {
        List<Getter> getters = new ArrayList<>();
        Method[] methods = clazz.getMethods();
        getMethodsStartWithGetOrIs(methods, getters);
        if (!getters.isEmpty()) {
            for (Getter getter : getters) {
                String methodName = null;
                methodName = getMethodName(getter.getMethod(), methodName);
                var tempMethodName = methodName.toCharArray();
                tempMethodName[0] = Character.toLowerCase(tempMethodName[0]);
                getter.setName(new String(tempMethodName));
            }
        }
        return getters;
    }

    private void getMethodsStartWithGetOrIs(Method[] methods, List<Getter> getters) {
        for (Method method : methods) {
            if (isCorrectGetMethod(method)) {
                getters.add(new Getter(method, null));
            }
        }
    }

    private static boolean isCorrectGetMethod(Method method) {
        return (method.getReturnType() != void.class)
                && (method.getParameterTypes().length == 0)
                && !method.getName().equals("getClass")
                && (method.getName().startsWith("get") || method.getName().startsWith("is"));
    }

    private String getMethodName(Method method, String methodName) {
        if (method.getName().startsWith("get")) {
            methodName = method.getName().replace("get", "");
        } else if (method.getName().startsWith("is")) {
            methodName = method.getName().replace("is", "");
        }
        return methodName;
    }

    @Override
    public ReflectionJsonSerializer serialize(T obj) {
        Map<String, Object> methodsAndValues = new HashMap<>();
        try {
            for (Getter getter : getters) {
                methodsAndValues.put(getter.getName(), getter.getMethod().invoke(obj));
            }
        } catch (Exception ignore) {
        }
        return new ReflectionJsonSerializer(methodsAndValues);
    }
}
