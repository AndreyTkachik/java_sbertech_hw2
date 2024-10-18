package org.example;

import java.lang.reflect.InvocationTargetException;

public interface Serializer {

    String serialize(Object obj) throws IllegalAccessException, InvocationTargetException;

    String serializeString(Object obj);

    String serializeCollection(Object obj) throws IllegalAccessException, InvocationTargetException;

    String serializeArray(Object obj) throws IllegalAccessException, InvocationTargetException;

    String serializePrimitive(Object obj);

    String serializeObject(Object obj) throws IllegalAccessException, InvocationTargetException;

    boolean checkPrimitive (Object obj);
}
