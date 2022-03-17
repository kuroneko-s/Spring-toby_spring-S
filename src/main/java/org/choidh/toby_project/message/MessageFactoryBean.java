package org.choidh.toby_project.message;

import org.springframework.beans.factory.FactoryBean;

public class MessageFactoryBean implements FactoryBean<Message> {

    String message = "Factory Bean";

    // factory bean의 프로퍼티로 설정해서 DI 받을수 있게 설정
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public Message getObject() throws Exception {
        return Message.newMessage(message);
    }

    @Override
    public Class<?> getObjectType() {
        return Message.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
