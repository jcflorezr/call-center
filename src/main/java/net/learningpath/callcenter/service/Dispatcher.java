package net.learningpath.callcenter.service;

import net.learningpath.callcenter.dto.Call;
import net.learningpath.callcenter.employee.factory.EmployeesLevel;
import net.learningpath.callcenter.event.listener.Listener;
import net.learningpath.callcenter.event.topic.EmployeesAvailabilityTopic;

public interface Dispatcher extends Listener {

    void dispatchCall(Call call, EmployeesLevel hierarchyLevel, EmployeesAvailabilityTopic employeesAvailability);

}
