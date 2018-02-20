package net.learningpath.callcenter.service;

import net.learningpath.callcenter.dto.request.Call;
import net.learningpath.callcenter.dto.response.Response;

public interface Dispatcher {

    Response dispatchCall(Call call);

}
