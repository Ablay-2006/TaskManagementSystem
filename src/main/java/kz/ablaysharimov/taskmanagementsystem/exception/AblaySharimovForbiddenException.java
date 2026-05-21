package kz.ablaysharimov.taskmanagementsystem.exception;

public class AblaySharimovForbiddenException extends RuntimeException {
    public AblaySharimovForbiddenException(String message) {
        super(message);
    }

    public AblaySharimovForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }
}

