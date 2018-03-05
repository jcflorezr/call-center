package net.learningpath.callcenter;

import net.learningpath.callcenter.dto.request.Call;
import net.learningpath.callcenter.dto.response.Response;

public interface CallCenterController {

    Response getRequest(Call call);

}
