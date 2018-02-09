package net.learningpath.callcenter.event.topic;

import net.learningpath.callcenter.dto.Call;

public interface EmployeesAvailabilityTopic extends Topic {

    void notifyAvailability();

    void notifyUnavailability(Call call);

}
