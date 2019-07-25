package com.github.lburgazzoli.graal;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Function;

import org.graalvm.polyglot.Context;

public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            throw new IllegalArgumentException("Usage:  com.github.lburgazzoli.graal.main file");
        }

        try(Context ctx = Context.newBuilder("js").allowAllAccess(true).build()) {
            ctx.getBindings("js").putMember("__dsl", new DSL());
            byte[] content = Files.readAllBytes(Paths.get(args[0]));
            ctx.eval("js", "with (__dsl) { " + new String(content) + "}");
        }
    }

    public static class DSL {
        public void sayHello(String target) {
            System.out.println("Hello " + target);
        }

        public void exec(Function<String, String> function) {
            System.out.println(function.apply("dsl"));
        }
    }
}
