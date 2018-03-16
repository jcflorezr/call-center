package net.learningpath.callcenter.dto.response.failed.badrequest;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CallEmptyErrorResponse extends BadRequestErrorResponse {

    public CallEmptyErrorResponse() {
        super(null, "Call info cannot be empty");
    }

    @Override
    public List<String> getHints() {
        return io.vavr.collection.List.of("Please add the call info with the following structure: {'clientName': 'client name'}").toJavaList();
    }

}
