package org.example;

import java.lang.reflect.Method;

class Getter {
    private Method method;
    private String name;

    public Getter(Method methodList, String varsNames) {
        this.method = methodList;
        this.name = varsNames;
    }

    public Method getMethod() {
        return method;
    }

    public String getName() {
        return name;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public void setName(String name) {
        this.name = name;
    }
}
