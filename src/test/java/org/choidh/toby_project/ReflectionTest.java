package org.choidh.toby_project;

import org.choidh.toby_project.reflect.Hello;
import org.choidh.toby_project.reflect.HelloTarget;
import org.choidh.toby_project.reflect.HelloUppercase;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReflectionTest {

    @Test
    public void reflectTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String str = "Hello";

        assertEquals(str.length(), 5);

        Method length = str.getClass().getMethod("length");
        int len = (int) length.invoke(str);

        assertEquals(len, 5);
        assertEquals(str.charAt(0), 'H');

        Method charAt = String.class.getMethod("charAt", int.class);

        assertEquals((Character) charAt.invoke(str, 0), 'H');

        Method[] methods = String.class.getMethods();

        Arrays.stream(methods).forEach(method -> System.out.println(method.toString()));
    }

    @Test
    public void simpleHello() {
        Hello hello = new HelloTarget();
        assertEquals(hello.sayHello("Toby"), "Hello Toby");
        assertEquals(hello.sayHi("Toby"), "Hi Toby");
        assertEquals(hello.sayThankYou("Toby"), "Thank You Toby");
    }

    @Test
    public void uppercaseHello() {
        Hello proxyHello = new HelloUppercase(new HelloTarget());

        assertEquals(proxyHello.sayHello("Toby"), "HELLO TOBY");
        assertEquals(proxyHello.sayHi("Toby"), "HI TOBY");
        assertEquals(proxyHello.sayThankYou("Toby"), "THANK YOU TOBY");
    }

}
