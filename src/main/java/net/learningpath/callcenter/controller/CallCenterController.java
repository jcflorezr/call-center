package net.learningpath.callcenter.controller;

import net.learningpath.callcenter.dto.request.Call;
import net.learningpath.callcenter.dto.response.Response;
import org.springframework.http.ResponseEntity;

public interface CallCenterController {

    ResponseEntity<Response> getRequest(Call call);

}
