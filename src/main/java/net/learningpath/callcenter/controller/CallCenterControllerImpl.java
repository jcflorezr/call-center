package net.learningpath.callcenter.controller;

import io.vavr.control.Option;
import net.learningpath.callcenter.dto.request.Call;
import net.learningpath.callcenter.dto.response.*;
import net.learningpath.callcenter.dto.response.failed.badrequest.BadRequestErrorResponse;
import net.learningpath.callcenter.dto.response.failed.badrequest.CallEmptyErrorResponse;
import net.learningpath.callcenter.dto.response.failed.servererror.InternalServerErrorResponse;
import net.learningpath.callcenter.dto.response.success.SuccessResponse;
import net.learningpath.callcenter.service.Dispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Objects;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.Predicates.instanceOf;

@Controller
@PropertySource("classpath:test-call-center-calls-endpoint.properties")
@RequestMapping("${endpoint-version}${context-name}")
public class CallCenterControllerImpl implements CallCenterController {

    @Autowired
    private Dispatcher dispatcher;

    @Override
    @PostMapping(value = "${resource}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseEntity<Response> getRequest(@RequestBody Call call) {
        Response response = Option.of(call)
                            .filter(this::isValidCall)
                            .map(dispatcher::dispatchCall)
                            .getOrElse(new CallEmptyErrorResponse());
        return Match(response).of(
                Case($(instanceOf(SuccessResponse.class)), ResponseEntity.ok(response)),
                Case($(instanceOf(BadRequestErrorResponse.class)), ResponseEntity.badRequest().body(response)),
                Case($(instanceOf(InternalServerErrorResponse.class)), ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response))
        );
    }

    private boolean isValidCall(Call currentCall) {
        return Option.of(currentCall)
                .filter(Objects::nonNull)
                .flatMap(call -> Option.of(call.getClientName()))
                .filter(clientName -> !clientName.isEmpty())
                .map(clientName -> true)
                .getOrElse(false);
    }

}
