package org.choidh.toby_project.vol2;

import org.choidh.toby_project.vol2.chap1.Hello;
import org.choidh.toby_project.vol2.chap1.StringPrinter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "classpath:/default-context.xml")
@Configuration
@EnableAspectJAutoProxy
public class chap1Test {

    @Test
    public void definitionTest() {
        StaticApplicationContext context = new StaticApplicationContext();
        context.registerSingleton("hello1", Hello.class);
        context.registerSingleton("hello2", Hello.class);
        context.registerSingleton("hello3", Hello.class);
        context.registerSingleton("hello4", Hello.class);

        Hello hello1 = context.getBean("hello4", Hello.class);
        hello1.println();

        Iterator<String> iterator = context.getBeanFactory().getBeanNamesIterator();
        while(iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }

    @Test
    public void rootDefinition() {
        StaticApplicationContext context = new StaticApplicationContext();
        context.registerSingleton("hello", Hello.class);

        RootBeanDefinition helloDef = new RootBeanDefinition(Hello.class);
        helloDef.getPropertyValues().addPropertyValue("name", "Spring");
        context.registerBeanDefinition("hello2", helloDef);

        Hello hello1 = context.getBean("hello", Hello.class);
        Hello hello2 = context.getBean("hello2", Hello.class);
        hello1.println();
        hello2.println();

        Arrays.stream(context.getBeanFactory().getBeanDefinitionNames()).forEach(System.out::println);
    }

    @Test
    public void definitionWithDI() {
        StaticApplicationContext context = new StaticApplicationContext();
        context.registerBeanDefinition("printer", new RootBeanDefinition(StringPrinter.class));

        RootBeanDefinition helloDef = new RootBeanDefinition(Hello.class);
        helloDef.getPropertyValues().addPropertyValue("name", "Spring");
        helloDef.getPropertyValues().addPropertyValue("printer", new RuntimeBeanReference("printer")); // DI

        context.registerBeanDefinition("hello", helloDef);

        Hello hello = context.getBean("hello", Hello.class);
        hello.printWithName();

        assertNotEquals(context.getBean("printer"), null);
    }

    @Test
    public void xmlReaderTest() {
        /*
            GenericApplicationContext context = new GenericApplicationContext();
            XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(context);
            reader.loadBeanDefinitions("classpath:/default-context.xml");

            context.refresh(); // 초기화 명령
        */

        // 합친 클래스
        GenericXmlApplicationContext context = new GenericXmlApplicationContext("classpath:/default-context.xml");


        Hello hello = context.getBean("hello", Hello.class);
        hello.printWithName();
    }

    @Autowired
    ApplicationContext testContext;

    @Test
    void testAppContext() {
        System.out.println(testContext.getClass().getName()); // GenericApplicationContext
    }

    @Test
    void contextLevel_Parent() {
        GenericXmlApplicationContext context = new GenericXmlApplicationContext("classpath:/parent-context.xml");

        Hello hello = context.getBean("hello", Hello.class);
        hello.printWithName();
    }

    @Test
    void contextLevel_Child() {
        GenericXmlApplicationContext parentContext = new GenericXmlApplicationContext("classpath:/parent-context.xml");
        GenericApplicationContext childContext = new GenericApplicationContext(parentContext);
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(childContext);
        reader.loadBeanDefinitions("classpath:/child-context.xml");

        childContext.refresh();

        Hello hello = childContext.getBean("hello", Hello.class);
        hello.printWithName();


    }

    @Test
    public void enumTest() {
        Status status = Status.결제대기;
        System.out.println();

        List<Integer> list = new ArrayList<>() {
            {
                add(1); add(3); add(50); add(99);
            }
        };

        Comparator<Integer> comparator = (t1, t2) -> t1 - t2;

        Collections.sort(list, comparator.reversed());

        System.out.println(list.toString());
        throw new RuntimeException();
    }

    enum Status {
        결제대기(0), 결제완료(1), 주문(2), 배송중(3), 배송완료(4);

        private int value;

        Status(int value) {
            this.value = value;
        }
    }

//    private ParentClass parentClass;
    @Autowired
    private Map<String, FamilyClass> familyClass;
    @Autowired
    private FamilyClass childClass;

    @Autowired
    private ParentClass parentClass;

    @Test
    public void autowiredTest() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ComponentConfig.class);
        FamilyClass familyClass = context.getBean("familyClass", FamilyClass.class);
        InnerClass innerClass = context.getBean("innerClass", InnerClass.class);

        assertEquals(familyClass.hashCode(), innerClass.getFamilyClass().hashCode());
    }

   /* @Test
    public void extendsTest() {
        ParentClass parentClass = new ParentClass();
        parentClass.hello();

        ParentClass parentClass1 = new ChildClass();
        parentClass1.hello();
    }

    @Test
    public void extendsTest2() {
        ParentClass parentClass = ParentClass.of();
        parentClass.hello();

        ParentClass childClass = ChildClass.of();
        childClass.hello();
    }

    @Test
    public void suplierTest() {
        Supplier<ParentClass> supplier = ChildClass::new;

        ParentClass parentClass = supplier.get();
        parentClass.hello();
    }*/

}
