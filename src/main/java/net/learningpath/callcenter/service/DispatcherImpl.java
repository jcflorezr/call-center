package net.learningpath.callcenter.service;

import io.vavr.CheckedRunnable;
import io.vavr.control.Option;
import io.vavr.control.Try;
import net.learningpath.callcenter.dto.request.Call;
import net.learningpath.callcenter.dto.response.ErrorResponse;
import net.learningpath.callcenter.dto.response.Response;
import net.learningpath.callcenter.dto.response.SuccessResponse;
import net.learningpath.callcenter.employee.Employee;
import net.learningpath.callcenter.employee.hierarchylevel.EmployeesLevel;
import net.learningpath.callcenter.event.EmployeesAvailabilityTopic;
import net.learningpath.callcenter.exceptions.HierarchyLevelException;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DispatcherImpl implements Dispatcher {

    private ExecutorService executorService;
    private EmployeesAvailabilityTopic employeesAvailability;
    private EmployeesLevel employeesLevel;

    public DispatcherImpl(EmployeesAvailabilityTopic employeesAvailability, EmployeesLevel employeesLevel) {
        this.executorService = Executors.newSingleThreadExecutor();
        this.employeesAvailability = employeesAvailability;
        this.employeesLevel = employeesLevel;
    }

    @Override
    public synchronized Response dispatchCall(Call call) {
        Try<Employee> dispatch =
            employeesLevel.getAvailableEmployee()
                .orElse(
                        () -> Try.run(() -> employeesAvailability.notifyUnavailability(this))
                                .andThen(() -> System.out.println("There is no availability... Enqueuing call of client: " + call.getClientName()))
                                .andThenTry((CheckedRunnable) this::wait).onFailure(HierarchyLevelException::failedWhenPuttingCallOnHold)
                                .transform(Void -> employeesLevel.getAvailableEmployee()))
                .toTry()
                .mapTry(employee -> employee.receiveCall(call))
                .andThenTry(employeesLevel::returnEmployeeToQueue)
                .andFinally(() -> executorService.execute(employeesAvailability::notifyAvailability));

        return Option.of(dispatch)
                .filter(Try::isFailure)
                .toTry()
                .map(employeeTry -> employeeTry.failed().map(exception -> ErrorResponse.newResponse(call, exception)))
                .getOrElse(() -> Try.of(() -> SuccessResponse.newResponse(call)))
                .get();
    }

}
