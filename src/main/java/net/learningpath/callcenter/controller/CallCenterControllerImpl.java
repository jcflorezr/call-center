package net.learningpath.callcenter.controller;

import io.vavr.control.Option;
import net.learningpath.callcenter.dto.request.Call;
import net.learningpath.callcenter.dto.response.Response;
import net.learningpath.callcenter.dto.response.failed.servererror.InternalServerErrorResponse;
import net.learningpath.callcenter.dto.response.success.SuccessResponse;
import net.learningpath.callcenter.service.Dispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.Predicates.instanceOf;

@RestController
@PropertySource("classpath:call-center-calls-endpoint.properties")
@RequestMapping("${endpoint-version}${context-name}")
public class CallCenterControllerImpl implements CallCenterController {

    @Autowired
    private Dispatcher dispatcher;

    @Override
    @PostMapping(value = "${resource}${client-name-path-variable}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseEntity<Response> getRequest(@PathVariable String clientName) {
        Response response = Option.of(clientName)
                            .map(Call::new)
                            .map(dispatcher::dispatchCall)
                            .get();
        return Match(response).of(
                Case($(instanceOf(SuccessResponse.class)), ResponseEntity.ok(response)),
                Case($(instanceOf(InternalServerErrorResponse.class)), ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response))
        );
    }

}
