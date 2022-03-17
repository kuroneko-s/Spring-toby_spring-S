package org.choidh.toby_project.exception;

public class SqlRetrievalFailureException extends RuntimeException{
    public SqlRetrievalFailureException() {
    }

    public SqlRetrievalFailureException(String message, Throwable cause) {
        super(message, cause);
    }

    public SqlRetrievalFailureException(String message) {
        super(message);
    }

    public SqlRetrievalFailureException(Throwable cause) {
        super(cause);
    }
}
