package net.learningpath.callcenter.dto.response;

public class Success implements Response {

    private boolean success;
    private String message;

    public static Response getInstance() {
        return new Success();
    }

    private Success() {
        this.success = true;
        this.message = "Call was attended successfully !!!";
    }

    @Override
    public boolean isSuccess() {
        return success;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
