package net.learningpath.callcenter.dto.response;

import net.learningpath.callcenter.dto.request.Call;

public interface Response {

    Call getCall();

    boolean isSuccess();

    default String getErrorType() {return null;}

    String getMessage();

    default DetailedError getDetails() {
        return null;
    }

}
