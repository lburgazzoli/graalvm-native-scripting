package com.github.lburgazzoli.graal;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.graalvm.nativeimage.hosted.RuntimeReflection;

public class Feature implements org.graalvm.nativeimage.hosted.Feature {
    @Override
    public void beforeAnalysis(BeforeAnalysisAccess access) {
        register(Main.DSL.class);
    }

    private static void register(Class<?> cl) {
        RuntimeReflection.register(cl);

        for (Constructor<?> c : cl.getConstructors()) {
            RuntimeReflection.register(c);
        }
        for (Method method : cl.getMethods()) {
            RuntimeReflection.register(method);
        }
    }
}
