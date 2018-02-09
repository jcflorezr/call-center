package net.learningpath.callcenter.exceptions;

public class DispatcherException extends RuntimeException {

    private DispatcherException(String message) {
        super(message);
    }

    public static void failedWhenEnqueuingCall(Throwable e) {
        throw new DispatcherException("Failed while trying to enqueue the unanswered call: " + e);
    }

    public static void failedWhenDequeuingCall(Throwable e) {
        throw new DispatcherException("Failed while trying to dequeue the unanswered call: " + e);
    }

}
