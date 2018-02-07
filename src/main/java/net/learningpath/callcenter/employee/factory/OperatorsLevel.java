package net.learningpath.callcenter.employee.factory;

import net.learningpath.callcenter.employee.Operator;

import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class OperatorsLevel implements EmployeesLevel {

    private EmployeesLevel nextHierarchyLevel;
    private BlockingQueue<Operator> operators;

    private OperatorsLevel(EmployeesLevel nextHierarchyLevel, int numOfAvailableEmployees) {
        this.nextHierarchyLevel = checkNextHierarchyLevel(nextHierarchyLevel);
        this.operators = initializeHierarchyLevel(numOfAvailableEmployees);
    }

    public static OperatorsLevel newHierarchyLevel(EmployeesLevel nextHierarchyLevel, int numOfAvailableEmployees) {
        return new OperatorsLevel(nextHierarchyLevel, numOfAvailableEmployees);
    }

    private EmployeesLevel checkNextHierarchyLevel(EmployeesLevel nextHierarchyLevel) {
        Optional.of(nextHierarchyLevel instanceof OperatorsLevel)
                .orElseThrow(() -> new RuntimeException("Next hierarchy level cannot be the same as current hierachy level."));
        return nextHierarchyLevel;
    }

    private BlockingQueue<Operator> initializeHierarchyLevel(int numOfAvailableEmployees) {
        return IntStream.range(0, numOfAvailableEmployees)
                .mapToObj(i -> new Operator())
                .collect(Collectors.toCollection(() -> new ArrayBlockingQueue(numOfAvailableEmployees)));
    }

}
