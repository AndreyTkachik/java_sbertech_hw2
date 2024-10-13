package org.example;

import org.mdkt.compiler.InMemoryJavaCompiler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add(null);
        String[] Array = {"aaaa", null};
        BookTest book1 = new BookTest("A", "A", 13, list, Array, false);
        JSONGenerator test = new JSONGenerator();
        System.out.println(test.generateJSON(book1));
        GeneratorBuilder builder = new GeneratorBuilder();
        String code = builder.buildCode(BookTest.class);
        Class<?> generatorClass = InMemoryJavaCompiler.newInstance()
                .compile("org.example." + builder.getLatestGeneratorName(), code);
        Object instance = generatorClass.getDeclaredConstructor().newInstance();
        Method method = generatorClass.getDeclaredMethod("generateJSON", BookTest.class);
        System.out.println(method.invoke(instance, book1));
    }
}