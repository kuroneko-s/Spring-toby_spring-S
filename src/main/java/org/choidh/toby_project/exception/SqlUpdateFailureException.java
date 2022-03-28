package org.choidh.toby_project.exception;

public class SqlUpdateFailureException extends RuntimeException {

    public SqlUpdateFailureException() {
    }

    public SqlUpdateFailureException(String message) {
        super(message);
    }

    public SqlUpdateFailureException(String message, Throwable cause) {
        super(message, cause);
    }

    public SqlUpdateFailureException(Throwable cause) {
        super(cause);
    }
}
