package org.example;

public interface SerializerFactory<T> {
    Object getInstance(Class<T> clazz) throws Exception;
}
