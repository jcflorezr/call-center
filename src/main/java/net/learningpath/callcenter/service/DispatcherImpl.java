package net.learningpath.callcenter.service;

import io.vavr.control.Option;
import net.learningpath.callcenter.dto.request.Call;
import net.learningpath.callcenter.dto.response.ErrorResponse;
import net.learningpath.callcenter.dto.response.Response;
import net.learningpath.callcenter.dto.response.SuccessResponse;
import net.learningpath.callcenter.employee.hierarchylevel.DirectorsLevel;
import net.learningpath.callcenter.employee.hierarchylevel.EmployeesLevel;
import net.learningpath.callcenter.employee.hierarchylevel.OperatorsLevel;
import net.learningpath.callcenter.employee.hierarchylevel.SupervisorsLevel;
import net.learningpath.callcenter.event.listener.AvailabilityListener;
import net.learningpath.callcenter.event.topic.EmployeesAvailability;
import net.learningpath.callcenter.event.topic.EmployeesAvailabilityTopic;
import net.learningpath.callcenter.exceptions.DispatcherException;
import net.learningpath.callcenter.exceptions.InternalServerException;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class DispatcherImpl implements Dispatcher, AvailabilityListener {

    private final ExecutorService executorService;
    private final EmployeesLevel directorsLevel;
    private final EmployeesLevel supervisorsLevel;
    private final EmployeesLevel operatorsLevel;
    private static final int NUM_OF_DIRECTORS = 1;
    private static final int NUM_OF_SUPERVISORS = 5;
    private static final int NUM_OF_OPERATORS = 10;
    private final EmployeesAvailabilityTopic employeesAvailability;
    private final BlockingQueue<Call> calls;

    private DispatcherImpl() {
        executorService = Executors.newFixedThreadPool(100);
        calls = new LinkedBlockingQueue<>();
        // Chain of Responsibility
        directorsLevel = DirectorsLevel.newHierarchyLevel(null, NUM_OF_DIRECTORS);
        supervisorsLevel = SupervisorsLevel.newHierarchyLevel(directorsLevel, NUM_OF_SUPERVISORS);
        operatorsLevel = OperatorsLevel.newHierarchyLevel(supervisorsLevel, NUM_OF_OPERATORS);
        // Observer
        employeesAvailability = EmployeesAvailability.getInstance();
        employeesAvailability.register(this);
    }

    public static DispatcherImpl getInstance() {
        return DispatcherHandler.INSTANCE;
    }

    private static class DispatcherHandler {
        private DispatcherHandler() {}
        private static final DispatcherImpl INSTANCE = new DispatcherImpl();
    }

    @Override
    public Response dispatchCall(Call call) {
        try {
            operatorsLevel.answerCall(call, employeesAvailability);
            return SuccessResponse.newResponse(call);
        } catch (InternalServerException e) {
            return ErrorResponse.newResponse(call, e);
        }
    }

    @Override
    public void update() {
        System.out.println("Availability is back!!!! Dispatching enqueued calls again...");
        Option.of(calls.poll())
                .peek(enqueuedCall -> executorService.execute(() -> dispatchCall(enqueuedCall)))
                .onEmpty(() -> System.out.println("There are no more enqueued calls!!!"));
    }

    @Override
    public void update(Call unansweredCall) {
        System.out.println("There is no availability... Enqueuing call of client: " + unansweredCall.getClientName());
        Option.of(unansweredCall)
                .toTry()
                .andThenTry(calls::put)
                .onFailure(DispatcherException::failedWhenEnqueuingCall);
    }

}
