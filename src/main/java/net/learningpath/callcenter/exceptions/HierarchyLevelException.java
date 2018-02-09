package net.learningpath.callcenter.exceptions;

public class HierarchyLevelException extends RuntimeException {

    private HierarchyLevelException(String message) {
        super(message);
    }

    public static void currentLevelSameAsNext() {
        throw new HierarchyLevelException("Next hierarchy level cannot be the same as current hierachy level.");
    }

    public static HierarchyLevelException levelNotInitialized (Throwable e) {
        return new HierarchyLevelException("Could not initialize the new employee for hierarchy level: " + e);
    }

    public static void failedWhenRetrievingEmployee (Throwable e) {
        throw new RuntimeException("Could not take an employee to answer the call. Reason: " + e);
    }

    public static void failedWhenRelocatingEmployee(Throwable e) {
        throw new RuntimeException("Call was processed successfully but the employee could not get back " +
                "to the availability queue. Reason: " + e);
    }

}
