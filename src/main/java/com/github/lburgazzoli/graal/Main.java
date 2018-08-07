package com.github.lburgazzoli.graal;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;
import org.graalvm.polyglot.proxy.ProxyExecutable;
import org.graalvm.polyglot.proxy.ProxyObject;

public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            throw new IllegalArgumentException("Usage:  com.github.lburgazzoli.graal.main file");
        }

        try(Context ctx = Context.create()) {
            final ProxyExecutable sayHello =  new ProxyExecutable() {
                @Override
                public Object execute(Value... arguments) {
                    if (arguments.length != 1) {
                        throw new IllegalArgumentException();
                    }

                    System.out.printf("Hello, %s\n", arguments[0].asString());
                    return null;
                }
            };

            final Map<String, Object> proxy = new HashMap<>();
            proxy.put("sayHello", sayHello);

            ctx.getBindings("js").putMember("bean", ProxyObject.fromMap(proxy));
            ctx.getBindings("js").putMember("sayHello", sayHello);

            ctx.eval(
                Source.newBuilder("js", new File(args[0])).build()
            );
        }
    }
}
