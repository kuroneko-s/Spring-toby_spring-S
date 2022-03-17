package factoryT;

import org.choidh.toby_project.message.Message;
import org.choidh.toby_project.message.MessageFactoryBean;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "/config/application-context.xml")
class MessageFactoryBeanTest {

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