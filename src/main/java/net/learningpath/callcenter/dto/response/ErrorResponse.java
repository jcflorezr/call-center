package net.learningpath.callcenter.dto.response;

public class Error implements Response {

    private boolean success;
    private String message;
    private DetailedError details;

    public static Response getInstance(Throwable cause) {
        return new Error(cause);
    }

    private Error(Throwable cause) {
        this.success = false;
        this.message = cause.getMessage();
        this.details = new DetailedError(cause);
    }

    @Override
    public boolean isSuccess() {
        return success;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public DetailedError getDetails() {
        return details;
    }

}
