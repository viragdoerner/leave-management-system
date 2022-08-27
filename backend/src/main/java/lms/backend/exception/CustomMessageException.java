package lms.backend.exception;

public class CustomMessageException extends RuntimeException {
    public CustomMessageException(String message) {
        super(message);
    }

    public CustomMessageException(String message, Throwable cause) {
        super(message, cause);
    }
}
