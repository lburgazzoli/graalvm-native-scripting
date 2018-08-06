package com.github.lburgazzoli.graal;

import java.io.File;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;
import org.graalvm.polyglot.proxy.ProxyExecutable;
import org.graalvm.polyglot.proxy.ProxyObject;

public class Main {
    public static void main(String[] args) throws Exception {
        try(Context ctx = Context.create()) {
            final MyBean bean = new MyBean();
            final MyBeanProxy proxy = new MyBeanProxy(bean);

            ctx.getBindings("js").putMember("bean", proxy);

            ctx.eval(
                Source.newBuilder("js", new File(args[0])).build()
            );
        }
    }

    public static class MyBeanProxy implements ProxyObject {
        private final MyBean bean;

        public MyBeanProxy(MyBean bean) {
            this.bean = bean;
        }

        @Override
        public Object getMember(String key) {
            if ("method".equals(key)) {
                return new ProxyExecutable() {
                    @Override
                    public Object execute(Value... arguments) {
                        bean.method();
                        return null;
                    }
                };
            }

            return null;
        }

        @Override
        public Object getMemberKeys() {
            return new String[] { "method" };
        }

        @Override
        public boolean hasMember(String key) {
            return "method".equals(key);
        }

        @Override
        public void putMember(String key, Value value) {
            throw new UnsupportedOperationException("removeMember() not supported.");
        }
    }

    public static class MyBean {
        public void method() {
            System.out.println("MyBean::method");
        }
    }
}
