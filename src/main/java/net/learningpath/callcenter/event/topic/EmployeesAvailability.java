package net.learningpath.callcenter.event.topic;

import net.learningpath.callcenter.dto.Call;
import net.learningpath.callcenter.event.listener.Listener;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class EmployeesAvailability implements EmployeesAvailabilityTopic {

    private List<Listener> listeners;
    private boolean availability;

    public EmployeesAvailability() {
        listeners = new CopyOnWriteArrayList<>();
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
        listeners.forEach(listener -> listener.update());
    }

    @Override
    public void notifyAvailability() {

    }

    @Override
    public void notifyUnavailability(Call call) {

    }

    @Override
    public Call getUnansweredCall() {
        return null;
    }
}
