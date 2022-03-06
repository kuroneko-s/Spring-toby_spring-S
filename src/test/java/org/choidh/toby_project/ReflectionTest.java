package org.choidh.toby_project;

import org.choidh.toby_project.reflect.Hello;
import org.choidh.toby_project.reflect.HelloTarget;
import org.choidh.toby_project.reflect.HelloUppercase;
import org.choidh.toby_project.reflect.UppercaseHandler;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;

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

    @Test
    public void uppercaseHelloWithProxy() {
        System.out.println(getClass().getClassLoader());

        Hello proxyHello = (Hello) Proxy.newProxyInstance(
                getClass().getClassLoader(), // 다이내믹 프록시 클래스의 로딩에 사용할 로더
                new Class[]{Hello.class}, // 구현할 인터페이스
                new UppercaseHandler(new HelloTarget()) // 부가기능이 담긴 InvocationHandler 구현체
        );

        assertEquals(proxyHello.sayHello("Toby"), "HELLO TOBY");
        assertEquals(proxyHello.sayHi("Toby"), "HI TOBY");
        assertEquals(proxyHello.sayThankYou("Toby"), "THANK YOU TOBY");
    }

    @Test
    public void createInstanceTest() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
//        final Date date = (Date) Class.forName("java.util.Date").newInstance();
//        System.out.println(date.getTime());

        final Date date = (Date) Class.forName("java.util.Date").getDeclaredConstructor().newInstance();
        final DateFormat dateInstance = DateFormat.getDateInstance(DateFormat.FULL);
        System.out.println(dateInstance.format(date));

    }

}
