package org.choidh.toby_project.reflect;

public class HelloUppercase implements Hello {
    Hello hello; // target Class

    public HelloUppercase(Hello hello) {
        this.hello = hello;
    }

    @Override
    public String sayHello(String name) {
        return this.hello.sayHello(name).toUpperCase();
    }

    @Override
    public String sayHi(String name) {
        return this.hello.sayHi(name).toUpperCase();
    }

    @Override
    public String sayThankYou(String name) {
        return this.hello.sayThankYou(name).toUpperCase();
    }
}
