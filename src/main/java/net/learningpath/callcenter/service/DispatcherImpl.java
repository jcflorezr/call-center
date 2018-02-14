package net.learningpath.callcenter.service;

import io.vavr.control.Try;
import net.learningpath.callcenter.dto.request.Call;
import net.learningpath.callcenter.employee.hierarchylevel.DirectorsLevel;
import net.learningpath.callcenter.employee.hierarchylevel.EmployeesLevel;
import net.learningpath.callcenter.employee.hierarchylevel.OperatorsLevel;
import net.learningpath.callcenter.employee.hierarchylevel.SupervisorsLevel;
import net.learningpath.callcenter.event.listener.AvailabilityListener;
import net.learningpath.callcenter.event.topic.EmployeesAvailability;
import net.learningpath.callcenter.event.topic.EmployeesAvailabilityTopic;
import net.learningpath.callcenter.exceptions.DispatcherException;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class DispatcherImpl implements Dispatcher, AvailabilityListener {

    private final EmployeesLevel directorsLevel;
    private final EmployeesLevel supervisorsLevel;
    private final EmployeesLevel operatorsLevel;
    private static final int NUM_OF_DIRECTORS = 1;
    private static final int NUM_OF_SUPERVISORS = 5;
    private static final int NUM_OF_OPERATORS = 10;
    private final EmployeesAvailabilityTopic employeesAvailability;
    private final BlockingQueue<Call> calls;

    private DispatcherImpl() {
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
    public void dispatchCall(Call call) {
        operatorsLevel.answerCall(call, employeesAvailability);
    }

    @Override
    public void update() {
        Try.of(calls::take)
            .onSuccess(this::dispatchCall)
            .onFailure(DispatcherException::failedWhenDequeuingCall);
    }

    @Override
    public void update(Call unansweredCall) {
        Try.run(() -> calls.put(unansweredCall))
                .onFailure(DispatcherException::failedWhenEnqueuingCall);
    }

}
