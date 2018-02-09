package net.learningpath.callcenter.service;

import io.vavr.control.Try;
import net.learningpath.callcenter.dto.Call;
import net.learningpath.callcenter.employee.factory.DirectorsLevel;
import net.learningpath.callcenter.employee.factory.EmployeesLevel;
import net.learningpath.callcenter.employee.factory.OperatorsLevel;
import net.learningpath.callcenter.employee.factory.SupervisorsLevel;
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
    private final int numOfDirectors = 1;
    private final int numOfSupervisors = 5;
    private final int numOfOperators = 10;
    private final EmployeesAvailabilityTopic employeesAvailability;
    private final BlockingQueue<Call> calls;

    private DispatcherImpl() {
        calls = new LinkedBlockingQueue<>();
        // Chain of Responsibility
        directorsLevel = DirectorsLevel.newHierarchyLevel(null, numOfDirectors);
        supervisorsLevel = SupervisorsLevel.newHierarchyLevel(directorsLevel, numOfSupervisors);
        operatorsLevel = OperatorsLevel.newHierarchyLevel(supervisorsLevel, numOfOperators);
        // Observer
        employeesAvailability = EmployeesAvailability.getInstance();
        employeesAvailability.register(this);
    }

    public static DispatcherImpl getInstance() {
        return DispatcherHandler.INSTANCE;
    }

    private static class DispatcherHandler {
        private static final DispatcherImpl INSTANCE = new DispatcherImpl();
    }

    @Override
    public void dispatchCall(Call call) {
        operatorsLevel.answerCall(call, employeesAvailability);
    }

    @Override
    public void update() {
        Call enqueuedCall = Try.of(calls::take).onFailure(DispatcherException::failedWhenDequeuingCall).get();
        dispatchCall(enqueuedCall);
    }

    @Override
    public void update(Call unansweredCall) {
        Try.run(() -> calls.put(unansweredCall)).onFailure(DispatcherException::failedWhenEnqueuingCall);
    }

}
