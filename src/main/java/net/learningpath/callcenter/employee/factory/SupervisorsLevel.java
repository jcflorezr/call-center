package net.learningpath.callcenter.employee.factory;

import net.learningpath.callcenter.employee.Supervisor;

import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SupervisorsLevel implements EmployeesLevel {

    private EmployeesLevel nextHierarchyLevel;
    private BlockingQueue<Supervisor> supervisors;

    private SupervisorsLevel(EmployeesLevel nextHierarchyLevel, int numOfAvailableEmployees) {
        this.nextHierarchyLevel = checkNextHierarchyLevel(nextHierarchyLevel);
        this.supervisors = initializeHierarchyLevel(numOfAvailableEmployees);
    }

    public static SupervisorsLevel newHierarchyLevel(EmployeesLevel nextHierarchyLevel, int numOfAvailableEmployees) {
        return new SupervisorsLevel(nextHierarchyLevel, numOfAvailableEmployees);
    }

    private EmployeesLevel checkNextHierarchyLevel(EmployeesLevel nextHierarchyLevel) {
        Optional.of(nextHierarchyLevel instanceof SupervisorsLevel)
                .orElseThrow(() -> new RuntimeException("Next hierarchy level cannot be the same as current hierachy level."));
        return nextHierarchyLevel;
    }

    private BlockingQueue<Supervisor> initializeHierarchyLevel(int numOfAvailableEmployees) {
        return IntStream.range(0, numOfAvailableEmployees)
                .mapToObj(i -> new Supervisor())
                .collect(Collectors.toCollection(() -> new ArrayBlockingQueue(numOfAvailableEmployees)));
    }

}
