package org.example;

import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

class GeneratorBuilderTest {

    @Test
    void buildCodeBookTest() {
        try {
            String actualJSON = "{ \"author\": \"A\", \"title\": \"A\", \"pages\": 13, " +
                    "\"genres\": [\"a\",null], \"tags\": [\"aaaa\",null], \"read\": false}";
            List<String> list = new ArrayList<>();
            list.add("a");
            list.add(null);
            String[] array = {"aaaa", null};
            BookTest book1 = new BookTest("A", "A", 13, list, array, false);
            SerializerFactory<BookTest> serializerFactoryImpl = new SerializerFactoryImpl<>();
            var generatorClass = serializerFactoryImpl.getInstance(BookTest.class);
            Method method = generatorClass.getClass().getDeclaredMethod("generate", BookTest.class);
            JSONAssert.assertEquals(actualJSON, (String) method.invoke(generatorClass, book1), true);
        } catch (Exception ignore) {}
    }

    @Test
    void buildCodeSeveralBookTest() {
        try {
            String actualJSON1 = "{ \"author\": \"A\", \"title\": \"A\", \"pages\": 13, " +
                    "\"genres\": [\"a\",null], \"tags\": [\"aaaa\",null], \"read\": false}";
            String actualJSON2 = "{ \"author\": \"A\", \"title\": \"B\", \"pages\": 13, " +
                    "\"genres\": [\"a\",null], \"tags\": [\"aaaa\",null], \"read\": true}";
            String actualJSON3 = "{ \"author\": \"A\", \"title\": \"A\", \"pages\": 0, " +
                    "\"genres\": null, \"tags\": null, \"read\": true}";
            List<String> list = new ArrayList<>();
            list.add("a");
            list.add(null);
            String[] array = {"aaaa", null};
            BookTest book1 = new BookTest("A", "A", 13, list, array, false);
            BookTest book2 = new BookTest("B", "A", 13, list, array, true);
            BookTest book3= new BookTest("A", "A", 0, null, null, true);
            SerializerFactory<BookTest> serializerFactoryImpl = new SerializerFactoryImpl<>();
            var generatorClass = serializerFactoryImpl.getInstance(BookTest.class);
            Method method = generatorClass.getClass().getDeclaredMethod("generate", BookTest.class);
            JSONAssert.assertEquals(actualJSON1, (String) method.invoke(generatorClass, book1), true);
            JSONAssert.assertEquals(actualJSON2, (String) method.invoke(generatorClass, book2), true);
            JSONAssert.assertEquals(actualJSON3, (String) method.invoke(generatorClass, book3), true);
        } catch (Exception ignore) {}
    }

    @Test
    void buildCodeNullBookTest() {
        try {
            String actualJSON1 = "{ \"author\": null, \"title\": null, \"pages\": 0, " +
                    "\"genres\": null, \"tags\": null, \"read\": false}";
            BookTest book1 = new BookTest(null, null, 0, null, null, false);
            SerializerFactory<BookTest> serializerFactoryImpl = new SerializerFactoryImpl<>();
            var generatorClass = serializerFactoryImpl.getInstance(BookTest.class);
            Method method = generatorClass.getClass().getDeclaredMethod("generate", BookTest.class);
            JSONAssert.assertEquals(actualJSON1, (String) method.invoke(generatorClass, book1), true);
        } catch (Exception ignore) {}
    }
}