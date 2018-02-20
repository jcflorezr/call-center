package net.learningpath.callcenter.exceptions;

public class HierarchyLevelException extends InternalServerException {

    private HierarchyLevelException(String message) {
        super(message);
    }

    private HierarchyLevelException(String message, Throwable cause) {
        super(message, cause);
    }

    public static void currentLevelSameAsNext() {
        throw new HierarchyLevelException("Next hierarchy level cannot be the same as current hierachy level.");
    }

    public static HierarchyLevelException levelCouldNotBeInitialized(Throwable cause) {
        return new HierarchyLevelException("Could not initialize the new employee for hierarchy level.", cause);
    }

    public static void failedWhenRetrievingEmployee (Throwable cause) {
        throw new HierarchyLevelException("Could not take an employee to answer the call.", cause);
    }

    public static void failedWhenRelocatingEmployee(Throwable cause) {
        throw new HierarchyLevelException("Call was processed successfully but the employee could not get back " +
                "to the availability queue.", cause);
    }

    public static void failedWhileAttendingCall(Throwable cause) {
        throw new HierarchyLevelException("Call was lost during the process.", cause);
    }

}
