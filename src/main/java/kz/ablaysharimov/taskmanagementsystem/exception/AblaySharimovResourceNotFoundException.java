package kz.ablaysharimov.taskmanagementsystem.exception;

public class AblaySharimovResourceNotFoundException extends RuntimeException {
    public AblaySharimovResourceNotFoundException(String message) {
        super(message);
    }

    public AblaySharimovResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

