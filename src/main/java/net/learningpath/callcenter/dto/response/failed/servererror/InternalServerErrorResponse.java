package net.learningpath.callcenter.dto.response.failed.servererror;

import com.fasterxml.jackson.annotation.JsonInclude;
import net.learningpath.callcenter.dto.request.Call;
import net.learningpath.callcenter.dto.response.Response;
import net.learningpath.callcenter.dto.response.failed.ErrorResponse;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class InternalServerErrorResponse implements ErrorResponse {

    private Call call;
    private boolean success;
    private String errorType;
    private String message;
    private DetailedError details;

    public static Response newResponse(Call call, Throwable cause) {
        return new InternalServerErrorResponse(call, cause);
    }

    private InternalServerErrorResponse(Call call, Throwable cause) {
        this.call = call;
        this.success = false;
        this.errorType = cause.getClass().getName();
        this.message = cause.getMessage();
        this.details = new DetailedError(cause);
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
    public String getErrorType() {
        return errorType;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public DetailedError getDetails() {
        return details;
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "call=" + call +
                ", success=" + success +
                ", errorType='" + errorType + '\'' +
                ", message='" + message + '\'' +
                ", details=" + details +
                '}';
    }

}
