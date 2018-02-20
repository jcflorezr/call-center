package net.learningpath.callcenter.event.listener;

import net.learningpath.callcenter.dto.request.Call;

public interface AvailabilityListener extends Listener {

    void update(Call unansweredCall);

}
