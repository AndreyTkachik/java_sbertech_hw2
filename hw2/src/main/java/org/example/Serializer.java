package org.example;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

public interface Serializer {

    String serialize(Object obj) throws IllegalAccessException, InvocationTargetException;

    String serializeString(Object obj);

    String serialiseCollection (Object obj) throws IllegalAccessException, InvocationTargetException;

    String serialiseArray (Object obj) throws IllegalAccessException, InvocationTargetException;

    String serialisePrimitive (Object obj);

    String serialiseObject (Object obj) throws IllegalAccessException, InvocationTargetException;

    boolean checkPrimitive (Object obj);
}
