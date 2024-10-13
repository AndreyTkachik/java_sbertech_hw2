package org.example;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mdkt.compiler.InMemoryJavaCompiler;
import org.skyscreamer.jsonassert.JSONAssert;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GeneratorBuilderTest {

    @Test
    void buildCodeBookTest() throws Exception {
        String actualJSON = "{ \"author\": \"A\", \"title\": \"A\", \"pages\": 13, " +
                "\"genres\": [\"a\",null], \"tags\": [\"aaaa\",null], \"read\": false}";
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add(null);
        String[] Array = {"aaaa", null};
        BookTest book1 = new BookTest("A", "A", 13, list, Array, false);
        GeneratorBuilder builder = new GeneratorBuilder();
        String code = builder.buildCode(BookTest.class);
        Class<?> generatorClass = InMemoryJavaCompiler.newInstance()
                .compile("org.example." + builder.getLatestGeneratorName(), code);
        Object instance = generatorClass.getDeclaredConstructor().newInstance();
        Method method = generatorClass.getDeclaredMethod("generateJSON", BookTest.class);
        JSONAssert.assertEquals(actualJSON, (String) method.invoke(instance, book1), true);
    }
}