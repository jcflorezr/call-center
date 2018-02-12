package net.learningpath.callcenter.dto.response;

public interface Response {

    boolean isSuccess();

    String getMessage();

    default Response getSuccessResponse() {
        return new Success();
    }

    default Response getErrorResponse(Throwable cause) {
        return new Error(cause);
    }

    default DetailedError getDetails() {
        return null;
    }

}
