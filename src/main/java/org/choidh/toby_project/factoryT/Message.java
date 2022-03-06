package org.choidh.toby_project.factoryT;

public class Message {
    private String message;

    private Message(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public static Message newMessage(String message) {
        return new Message(message);
    }
}
