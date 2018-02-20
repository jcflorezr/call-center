package net.learningpath.callcenter.dto.request;

public class Call {

    private String clientName;

    public Call(String clientName) {
        this.clientName = clientName;
    }

    public String getClientName() {
        return clientName;
    }

    @Override
    public String toString() {
        return "Call{" +
                "clientName='" + clientName + '\'' +
                '}';
    }
}
