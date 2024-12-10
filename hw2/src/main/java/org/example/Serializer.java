package org.example;

import java.lang.reflect.InvocationTargetException;

public interface Serializer<T> {
    String serialize(T obj);
}
