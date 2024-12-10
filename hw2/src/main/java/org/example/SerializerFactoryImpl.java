package org.example;

import org.mdkt.compiler.InMemoryJavaCompiler;

public class SerializerFactoryImpl<T> implements SerializerFactory<T> {
    @Override
    public Object getInstance(Class<T> clazz) throws Exception {
        GeneratorBuilder builder = new GeneratorBuilder();
        String code = builder.buildCode(clazz);
        Class<?> generatorClass = InMemoryJavaCompiler.newInstance()
                .compile("org.clazz." + builder.getLatestGeneratorName(), code);
        return generatorClass.getDeclaredConstructor().newInstance();
    }
}
