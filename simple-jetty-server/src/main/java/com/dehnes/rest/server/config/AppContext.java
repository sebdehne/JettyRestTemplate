package com.dehnes.rest.server.config;

import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.Set;

import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoBuilder;
import org.picocontainer.adapters.InstanceAdapter;

public class AppContext {

    private final MutablePicoContainer pico;
    private Set<Class> classes;

    public AppContext() {
        pico = new PicoBuilder().withCaching().withJavaEE5Lifecycle().build();
        this.classes = new HashSet<>();
    }

    public <T> void addInstance(Class<T> type, T instance) {
        pico.addAdapter(new InstanceAdapter<>(instance.getClass(), instance));
        classes.add(type);
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
            classes.add(type);
            pico.addComponent(type);
            addConstructorDependencies(type);
        }
    }


    public void start() {
        pico.start();
    }

    public void stop() {
        pico.stop();
    }
}
