package net.learningpath.callcenter.dto.response;

public interface Response {

    boolean isSuccess();

    String getMessage();

    default DetailedError getDetails() {
        return null;
    }

}
