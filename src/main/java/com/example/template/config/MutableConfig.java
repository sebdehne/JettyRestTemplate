package com.example.template.config;

import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.Set;

import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoBuilder;

public class MutableConfig {

    private final MutablePicoContainer pico;
    private Set<Class> classes;

    public MutableConfig() {
        pico = new PicoBuilder().withCaching().build();
        this.classes = new HashSet<>();
    }

    public <T> T getInstance(Class<T> type) {
        addClass(type);
        return pico.getComponent(type);
    }

    private void addConstructorDependencies(Class<?> clazz) {
        for (Constructor constructor : clazz.getConstructors()) {
            for (Class paramClazz : constructor.getParameterTypes()) {
                addClass(paramClazz);
            }
        }
    }

    private void addClass(Class type) {
        if (!classes.contains(type)) {
            pico.addComponent(type);
            addConstructorDependencies(type);
        }
    }

}
