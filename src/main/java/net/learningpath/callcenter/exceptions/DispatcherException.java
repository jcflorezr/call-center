package net.learningpath.callcenter.exceptions;

public class DispatcherException extends InternalServerException {

    private DispatcherException(String message, Throwable cause) {
        super(message, cause);
    }

    public static void failedWhenEnqueuingCall(Throwable cause) {
        throw new DispatcherException("Failed while trying to enqueue the unanswered call.", cause);
    }

    public static void failedWhenDequeuingCall(Throwable cause) {
        throw new DispatcherException("Failed while trying to dequeue the unanswered call.", cause);
    }

}
