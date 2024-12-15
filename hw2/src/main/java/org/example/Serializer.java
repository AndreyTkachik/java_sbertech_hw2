package org.example;

public interface Serializer<T> {
    ReflectionJsonSerializer serialize(T obj);
}
