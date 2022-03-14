package aop;

import org.choidh.toby_project.aop.test.Bean;
import org.choidh.toby_project.aop.test.TestTarget;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AOPTest {

    @Test
    public void test() {
        final Method[] methods = TestTarget.class.getDeclaredMethods();

        for (Method method : methods) {
            System.out.println(method.toString());
        }
    }

    @Test
    public void methodSignaturePointcut() throws Exception{
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(
                "execution(public int org.choidh.toby_project.aop.test.TestTarget.minus(int,int) throws RuntimeException)"
        );


        assertEquals(pointcut.getClassFilter().matches(TestTarget.class) &&
                pointcut.getMethodMatcher()
                        .matches(TestTarget.class
                                .getMethod("minus", int.class, int.class), null), true);

        assertEquals(pointcut.getClassFilter().matches(TestTarget.class) &&
                pointcut.getMethodMatcher()
                        .matches(TestTarget.class
                                .getMethod("plus", int.class, int.class), null), false);

        assertEquals(pointcut.getClassFilter().matches(Bean.class) &&
                pointcut.getMethodMatcher()
                        .matches(TestTarget.class
                                .getMethod("method"), null), false);
    }

    @Test
    public void pointcutTest() throws Exception {
        this.targetClassPointcutMatches("execution(* *.*(int,int))", false, false, true, true, false, false);
    }

    private void targetClassPointcutMatches(String expression, boolean... expected) throws Exception{
        this.pointcutMatches(expression, expected[0], TestTarget.class, "hello");
        this.pointcutMatches(expression, expected[1], TestTarget.class, "hello", String.class);
        this.pointcutMatches(expression, expected[2], TestTarget.class, "plus", int.class, int.class);
        this.pointcutMatches(expression, expected[3], TestTarget.class, "minus", int.class, int.class);
        this.pointcutMatches(expression, expected[4], TestTarget.class, "method");
        this.pointcutMatches(expression, expected[5], Bean.class, "method");
    }

    private void pointcutMatches(String expression, Boolean expected, Class<?> clazz, String methodName, Class<?>... args) throws Exception {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(expression);

        assertEquals(
                pointcut.getClassFilter().matches(clazz) &&
                        pointcut.getMethodMatcher().matches(clazz.getMethod(methodName, args), null), expected
        );
    }
}
