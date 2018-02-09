package net.learningpath.callcenter.employee.factory;

import io.vavr.control.Option;
import io.vavr.control.Try;
import net.learningpath.callcenter.dto.Call;
import net.learningpath.callcenter.employee.Employee;
import net.learningpath.callcenter.event.topic.EmployeesAvailabilityTopic;
import net.learningpath.callcenter.exceptions.HierarchyLevelException;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class EmployeesLevel <EMPLOYEE extends Employee, LEVEL extends EmployeesLevel> {

    private Option<EmployeesLevel> nextHierarchyLevel;
    private BlockingQueue<EMPLOYEE> employees;
    private BiPredicate<EmployeesLevel, Class<LEVEL>> levelsAreSame =
            (nextLevel, currentLevelType) -> nextLevel != null && nextLevel.getClass().isInstance(currentLevelType);

    protected EmployeesLevel(EmployeesLevel nextHierarchyLevel, int numOfAvailableEmployees,
                             Class<LEVEL> currentHierarchyLevelType, Class<EMPLOYEE> employeesType) {
        this.nextHierarchyLevel = checkNextHierarchyLevel(nextHierarchyLevel, currentHierarchyLevelType);
        this.employees = initializeHierarchyLevel(numOfAvailableEmployees, employeesType);
    }

    private Option<EmployeesLevel> checkNextHierarchyLevel(EmployeesLevel nextHierarchyLevel, Class<LEVEL> levelClassType) {
        return Option.of(nextHierarchyLevel)
                .filter(nextLevel -> levelsAreSame.test(nextLevel, levelClassType))
                .onEmpty(HierarchyLevelException::currentLevelSameAsNext);
    }

    private BlockingQueue<EMPLOYEE> initializeHierarchyLevel(int numOfAvailableEmployees, Class<EMPLOYEE> employee) {
        return IntStream.range(0, numOfAvailableEmployees)
                .mapToObj(i -> Try.of(() -> employee.newInstance()).getOrElseThrow(HierarchyLevelException::levelNotInitialized))
                .collect(Collectors.toCollection(() -> new ArrayBlockingQueue<>(numOfAvailableEmployees)));
    }

    public void answerCall(Call call, EmployeesAvailabilityTopic employeesAvailability) {
        Option.of(employees)
                .filter(queue -> !queue.isEmpty())
                .onEmpty(() -> nextHierarchyLevel
                                .onEmpty(() -> employeesAvailability.notifyUnavailability(call))
                                .peek(nextLevel -> nextLevel.answerCall(call, employeesAvailability)))
                .toTry()
                .mapTry(BlockingQueue::take).onFailure(HierarchyLevelException::failedWhenRetrievingEmployee)
                .andThenTry(emp -> emp.receiveCall(call)).onFailure(HierarchyLevelException::failedWhileAttendingCall)
                .andThenTry(emp -> employees.put(emp)).onFailure(HierarchyLevelException::failedWhenRelocatingEmployee)
                .andThen(employeesAvailability::notifyAvailability);
    }

}
