package net.learningpath.callcenter.event;

import net.learningpath.callcenter.service.Dispatcher;

public interface EmployeesAvailabilityTopic {

    void notifyAvailability();

    void notifyUnavailability(Dispatcher dispatcher);

}
