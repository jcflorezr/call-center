package net.learningpath.callcenter.event.topic;

import io.vavr.control.Option;
import net.learningpath.callcenter.dto.request.Call;
import net.learningpath.callcenter.event.listener.AvailabilityListener;
import net.learningpath.callcenter.event.listener.Listener;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class EmployeesAvailability implements EmployeesAvailabilityTopic {

    private final Object lock;
    private List<Listener> listeners;
    private AtomicBoolean employeesAvailable;

    private EmployeesAvailability() {
        lock = new Object();
        listeners = new CopyOnWriteArrayList<>();
        employeesAvailable = new AtomicBoolean(true);
    }

    private static class EmployeesAvailabilityHolder {
        private EmployeesAvailabilityHolder() {}
        private static final EmployeesAvailability INSTANCE = new EmployeesAvailability();
    }

    public static EmployeesAvailability getInstance() {
        return EmployeesAvailabilityHolder.INSTANCE;
    }

    @Override
    public void register(Listener listener) {
        listeners.add(listener);
    }

    @Override
    public void unregister(Listener listener) {
        listeners.remove(listener);
    }

    @Override
    public void notifyListeners() {
        listeners.forEach(Listener::update);
    }

    @Override
    public void notifyAvailability() {
        synchronized (lock) {
            notifyListeners();
        }
    }

    @Override
    public void notifyUnavailability(Call call) {
        synchronized (lock) {
            Option.of(employeesAvailable.getAndSet(false))
                    .peek(wasAvailable -> listeners
                            .forEach(listener -> ((AvailabilityListener) listener).update(call)));
        }
    }

}
