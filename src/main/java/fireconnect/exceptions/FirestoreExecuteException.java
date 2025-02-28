package fireconnect.exceptions;

import fireconnect.types.FirestoreErrorType;
import lombok.Getter;

@Getter
public class FirestoreExecuteException extends RuntimeException {

    private final FirestoreErrorType errorType;

    public FirestoreExecuteException(FirestoreErrorType errorType, String message) {
        super(message);
        this.errorType = errorType;
    }

    public FirestoreExecuteException(FirestoreErrorType errorType, String message, Throwable cause) {
        super(message, cause);
        this.errorType = errorType;
    }
}