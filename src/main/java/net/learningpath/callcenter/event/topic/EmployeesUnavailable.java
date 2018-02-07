package net.learningpath.callcenter.event.topic;

import net.learningpath.callcenter.event.listener.Listener;

import java.util.ArrayList;
import java.util.List;

public class EmployeesUnavailable implements Topic {

    private List<Listener> listeners;

    public EmployeesUnavailable() {
        listeners = new ArrayList<>();
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

}
