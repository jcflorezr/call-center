package net.learningpath.callcenter.service;

import net.learningpath.callcenter.dto.Call;
import net.learningpath.callcenter.employee.factory.EmployeesLevel;
import net.learningpath.callcenter.event.topic.EmployeesAvailabilityTopic;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class DispatcherImpl implements Dispatcher {

    private final BlockingQueue<Call> calls;

    private DispatcherImpl() {
        calls = new LinkedBlockingQueue<>();
    }

    public static DispatcherImpl getInstance() {
        return DispatcherHandler.INSTANCE;
    }

    private static class DispatcherHandler {
        private static final DispatcherImpl INSTANCE = new DispatcherImpl();
    }

    @Override
    public void dispatchCall(Call call, EmployeesLevel hierarchyLevel, EmployeesAvailabilityTopic employeesAvailability) {

    }

    @Override
    public void update() {

    }

}
