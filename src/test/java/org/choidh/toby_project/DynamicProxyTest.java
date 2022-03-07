package org.choidh.toby_project;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.choidh.toby_project.reflect.Hello;
import org.choidh.toby_project.reflect.HelloTarget;
import org.choidh.toby_project.reflect.UppercaseHandler;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;

import java.lang.reflect.Proxy;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DynamicProxyTest {
    @Test
    public void simpleProxy() {
        Hello hello = (Hello) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{Hello.class},
                new UppercaseHandler(new HelloTarget())
        );
    }

    @Test
    public void proxyFactoryBean() {
        ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
        proxyFactoryBean.setTarget(new HelloTarget());
//        proxyFactoryBean.addAdvice(new UppercaseAdvice()); // 순수하게 추가기능만 정의

        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedName("sayH*"); // point cut 설정
        proxyFactoryBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UppercaseAdvice()));

        Hello proxyHello = (Hello) proxyFactoryBean.getObject();
        assertEquals(proxyHello.sayHello("Toby"), "HELLO TOBY");
        assertEquals(proxyHello.sayHi("Toby"), "HI TOBY");
        assertEquals(proxyHello.sayThankYou("Toby"), "Thank You Toby");
    }

    static class UppercaseAdvice implements MethodInterceptor {

        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            String ret = (String) invocation.proceed();
            return ret.toUpperCase();
        }
    }

}
