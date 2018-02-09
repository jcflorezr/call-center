package net.learningpath.callcenter.event.listener;

import net.learningpath.callcenter.dto.Call;

public interface AvailabilityListener extends Listener {

    void update(Call unansweredCall);

}
