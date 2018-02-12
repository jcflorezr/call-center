package net.learningpath.callcenter.service;

import net.learningpath.callcenter.dto.request.Call;

public interface Dispatcher {

    void dispatchCall(Call call);

}
