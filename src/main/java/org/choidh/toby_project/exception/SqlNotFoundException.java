package org.choidh.toby_project.exception;

public class SqlNotFoundException extends RuntimeException{

    public SqlNotFoundException() {
        super();
    }

    public SqlNotFoundException(String message) {
        super(message);
    }

    public SqlNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
