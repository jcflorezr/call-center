package net.learningpath.callcenter.service;

import io.vavr.CheckedRunnable;
import io.vavr.control.Try;
import net.learningpath.callcenter.dto.request.Call;
import net.learningpath.callcenter.dto.response.failed.servererror.InternalServerErrorResponse;
import net.learningpath.callcenter.dto.response.Response;
import net.learningpath.callcenter.dto.response.success.SuccessResponse;
import net.learningpath.callcenter.employee.hierarchylevel.EmployeesLevel;
import net.learningpath.callcenter.event.EmployeesAvailabilityTopic;
import net.learningpath.callcenter.exceptions.HierarchyLevelException;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;

@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class DispatcherImpl implements Dispatcher {

    private ExecutorService executorService;
    private EmployeesAvailabilityTopic employeesAvailability;
    private EmployeesLevel employeesLevel;

    public DispatcherImpl(EmployeesLevel employeesLevel, EmployeesAvailabilityTopic employeesAvailability) {
        this.executorService = Executors.newSingleThreadExecutor();
        this.employeesLevel = employeesLevel;
        this.employeesAvailability = employeesAvailability;
    }

    @Override
    public synchronized Response dispatchCall(Call call) {
        Try<Call> dispatchedCall =
            employeesLevel.getAvailableEmployee()
                .orElse(
                        () -> Try.run(() -> employeesAvailability.notifyUnavailability(this))
                                .andThen(() -> System.out.println("There is no availability... Enqueuing call of client: " + call.getClientName()))
                                .andThenTry((CheckedRunnable) this::wait).onFailure(HierarchyLevelException::failedWhenPuttingCallOnHold)
                                .transform(Void -> employeesLevel.getAvailableEmployee()))
                .toTry()
                .mapTry(employee -> employee.receiveCall(call))
                // TODO If the employee was not enqueued again, the app should recover from that exception
                // and send a success response anyway
                .andThenTry(employeesLevel::returnEmployeeToQueue)
                .map(employee -> new Call(call, employee))
                .andFinally(() -> executorService.execute(employeesAvailability::notifyAvailability));

        return Match(dispatchedCall).of(
                    Case($(Try::isSuccess), () -> SuccessResponse.newResponse(dispatchedCall.get())),
                    Case($(Try::isFailure), () -> dispatchedCall.failed().map(exception -> InternalServerErrorResponse.newResponse(call, exception)).get())
               );
    }

}
