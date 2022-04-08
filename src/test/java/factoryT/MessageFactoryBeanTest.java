package factoryT;

import org.choidh.toby_project.TestConfig;
import org.choidh.toby_project.message.Message;
import org.choidh.toby_project.message.MessageFactoryBean;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MessageFactoryBeanTest extends TestConfig {

    @Autowired
    ApplicationContext context;

    @Test
    public void test() {
        Object message = context.getBean("message");
        assertEquals(message.getClass(), Message.class);
        assertEquals(((Message)message).getMessage(), "Factory Bean");

        final Object beanFactory = context.getBean("&message"); // Factory Bean
        assertEquals(beanFactory.getClass(), MessageFactoryBean.class);
    }


}