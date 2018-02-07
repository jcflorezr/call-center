package net.learningpath.callcenter.employee.factory;

import net.learningpath.callcenter.employee.Director;

import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DirectorsLevel implements EmployeesLevel {

    private EmployeesLevel nextHierarchyLevel;
    private BlockingQueue<Director> directors;

    private DirectorsLevel(EmployeesLevel nextHierarchyLevel, int numOfAvailableEmployees) {
        this.nextHierarchyLevel = checkNextHierarchyLevel(nextHierarchyLevel);
        this.directors = initializeHierarchyLevel(numOfAvailableEmployees);
    }

    public static DirectorsLevel newHierarchyLevel(EmployeesLevel nextHierarchyLevel, int numOfAvailableEmployees) {
        return new DirectorsLevel(nextHierarchyLevel, numOfAvailableEmployees);
    }

    private EmployeesLevel checkNextHierarchyLevel(EmployeesLevel nextHierarchyLevel) {
        Optional.of(nextHierarchyLevel instanceof DirectorsLevel)
                .orElseThrow(() -> new RuntimeException("Next hierarchy level cannot be the same as current hierachy level."));
        return nextHierarchyLevel;
    }

    private BlockingQueue<Director> initializeHierarchyLevel(int numOfAvailableEmployees) {
        return IntStream.range(0, numOfAvailableEmployees)
                .mapToObj(i -> new Director())
                .collect(Collectors.toCollection(() -> new ArrayBlockingQueue(numOfAvailableEmployees)));
    }

}
