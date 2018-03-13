package net.learningpath.callcenter.controller;

import net.learningpath.callcenter.dto.request.Call;
import net.learningpath.callcenter.dto.response.ErrorResponse;
import net.learningpath.callcenter.dto.response.Response;
import net.learningpath.callcenter.dto.response.SuccessResponse;
import net.learningpath.callcenter.employee.hierarchylevel.EmployeesLevel;
import net.learningpath.callcenter.event.EmployeesAvailabilityTopic;
import net.learningpath.callcenter.service.Dispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.Predicates.instanceOf;

@Controller
@RequestMapping("/call-center")
public class CallCenterControllerImpl implements CallCenterController {

    @Autowired
    private Dispatcher dispatcher;

    @Override
    @PostMapping(value = "/call", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseEntity<Response> getRequest(@RequestBody Call call) {
        System.out.println("entra");
        Response response = dispatcher.dispatchCall(call);
        return Match(response).of(
                Case($(instanceOf(SuccessResponse.class)), ResponseEntity.status(HttpStatus.OK).body(response)),
                Case($(instanceOf(ErrorResponse.class)), ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response))
        );
    }

}
