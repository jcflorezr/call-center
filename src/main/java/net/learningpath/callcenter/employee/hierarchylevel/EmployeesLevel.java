package net.learningpath.callcenter.employee.hierarchylevel;

import io.vavr.control.Option;
import io.vavr.control.Try;
import net.learningpath.callcenter.dto.request.Call;
import net.learningpath.callcenter.employee.Employee;
import net.learningpath.callcenter.event.topic.EmployeesAvailabilityTopic;
import net.learningpath.callcenter.exceptions.HierarchyLevelException;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class EmployeesLevel <EMPLOYEE extends Employee, LEVEL extends EmployeesLevel> {

    private final ExecutorService executorService;
    private Option<EmployeesLevel> nextHierarchyLevel;
    private BlockingQueue<EMPLOYEE> employees;
    private BiPredicate<EmployeesLevel, Class<LEVEL>> levelsAreNotSame =
            (nextLevel, currentLevelType) -> nextLevel == null || !currentLevelType.isAssignableFrom(nextLevel.getClass());

    protected EmployeesLevel(EmployeesLevel nextHierarchyLevel, int numOfAvailableEmployees,
                             Class<LEVEL> currentHierarchyLevelType, Class<EMPLOYEE> employeesType) {
        this.executorService = Executors.newFixedThreadPool(10);
        this.nextHierarchyLevel = nextHierarchyLevel == null ? Option.none()
                                                             : checkNextHierarchyLevel(nextHierarchyLevel, currentHierarchyLevelType);
        this.employees = initializeHierarchyLevel(numOfAvailableEmployees, employeesType);
    }

    private Option<EmployeesLevel> checkNextHierarchyLevel(EmployeesLevel nextHierarchyLevel, Class<LEVEL> levelClassType) {
        return Option.of(nextHierarchyLevel)
                .filter(nextLevel -> levelsAreNotSame.test(nextLevel, levelClassType))
                .onEmpty(HierarchyLevelException::currentLevelSameAsNext);
    }

    private BlockingQueue<EMPLOYEE> initializeHierarchyLevel(int numOfAvailableEmployees, Class<EMPLOYEE> employee) {
        return IntStream.range(0, numOfAvailableEmployees)
                .mapToObj(i -> Try.of(employee::newInstance).getOrElseThrow(HierarchyLevelException::levelCouldNotBeInitialized))
                .collect(Collectors.toCollection(() -> new ArrayBlockingQueue<>(numOfAvailableEmployees)));
    }

    public void answerCall(Call call, EmployeesAvailabilityTopic employeesAvailability) {
        Option.of(employees.poll())
                .peek(employee ->
                        Try.of(() -> employee)
                        .andThenTry(emp -> emp.receiveCall(call)).onFailure(HierarchyLevelException::failedWhileAttendingCall)
                        .andThenTry(emp -> employees.put(emp)).onFailure(HierarchyLevelException::failedWhenRelocatingEmployee)
                        .andThen(() -> executorService.execute(employeesAvailability::notifyAvailability)))
                .onEmpty(() -> nextHierarchyLevel
                                .onEmpty(() -> executorService.execute(() -> employeesAvailability.notifyUnavailability(call)))
                                .peek(nextLevel -> nextLevel.answerCall(call, employeesAvailability)));

    }

}
