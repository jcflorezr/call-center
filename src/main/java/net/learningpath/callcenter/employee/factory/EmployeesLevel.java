package net.learningpath.callcenter.employee.factory;

import net.learningpath.callcenter.dto.Call;
import net.learningpath.callcenter.employee.Employee;
import net.learningpath.callcenter.event.topic.EmployeesAvailabilityTopic;

import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class EmployeesLevel <EMPLOYEE extends Employee, LEVEL extends EmployeesLevel> {

    private Optional<EmployeesLevel> nextHierarchyLevel;
    private BlockingQueue<EMPLOYEE> employees;
    private BiPredicate<EmployeesLevel, Class<LEVEL>> levelsAreSame =
            (nextLevel, currentLevelType) -> nextLevel != null && nextLevel.getClass().isInstance(currentLevelType);

    protected EmployeesLevel(EmployeesLevel nextHierarchyLevel, int numOfAvailableEmployees,
                             Class<LEVEL> currentHierarchyLevelType, Class<EMPLOYEE> employeesType) {
        this.nextHierarchyLevel = checkNextHierarchyLevel(nextHierarchyLevel, currentHierarchyLevelType);
        this.employees = initializeHierarchyLevel(numOfAvailableEmployees, employeesType);
    }

    protected Optional<EmployeesLevel> checkNextHierarchyLevel(EmployeesLevel nextHierarchyLevel, Class<LEVEL> levelClassType) {
        Optional.of(levelsAreSame.test(nextHierarchyLevel, levelClassType))
                .orElseThrow(() -> new RuntimeException("Next hierarchy level cannot be the same as current hierachy level."));
        return Optional.ofNullable(nextHierarchyLevel);
    }

    protected  BlockingQueue<EMPLOYEE> initializeHierarchyLevel(int numOfAvailableEmployees, Class<EMPLOYEE> employee) {
        return IntStream.range(0, numOfAvailableEmployees)
                .mapToObj(i -> newEmployeeInstance(employee))
                .collect(Collectors.toCollection(() -> new ArrayBlockingQueue(numOfAvailableEmployees)));
    }

    private Employee newEmployeeInstance(Class<EMPLOYEE> employeeType) {
        try {
            return employeeType.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Could not initialize the new employee for hierarchy level: " + e);
        }
    }

    public void answerCall(Call call, EmployeesAvailabilityTopic employeesAvailability) {
        if (employees.isEmpty()) {
            if (nextHierarchyLevel.isPresent()) {
                nextHierarchyLevel.get().answerCall(call, employeesAvailability);
            } else {
                employeesAvailability.notifyAvailability();
            }
        } else {
            try {
                EMPLOYEE employee = employees.take();
                employee.receiveCall(call);
                employees.put(employee);
            } catch (InterruptedException | RuntimeException e) {
                e.printStackTrace();
            }
        }
    }

}
