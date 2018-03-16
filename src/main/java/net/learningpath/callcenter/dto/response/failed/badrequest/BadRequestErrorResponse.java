package net.learningpath.callcenter.dto.response.failed.badrequest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import net.learningpath.callcenter.dto.request.Call;

import net.learningpath.callcenter.dto.response.failed.ErrorResponse;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CallEmptyErrorResponse.class, name = "CallEmptyErrorResponse")})
public abstract class BadRequestErrorResponse implements ErrorResponse {

    @JsonProperty
    private Call call;
    @JsonProperty
    private boolean success;
    @JsonProperty
    private String message;

    protected BadRequestErrorResponse(Call call, String message) {
        this.call = call;
        this.message = message;
        this.success = false;
    }

    @Override
    public Call getCall() {
        return call;
    }

    @Override
    public boolean isSuccess() {
        return success;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public abstract List<String> getHints();
}
