package net.learningpath.callcenter.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.vavr.control.Option;
import net.learningpath.callcenter.employee.Employee;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Call {

    private String clientName;
    private Employee attendedBy;

    public Call() {}

    public Call(String clientName) {
        this.clientName = clientName;
    }

    public Call(String clientName, Employee employee) {
        this.clientName = clientName;
        this.attendedBy = employee;
    }

    public Call(Call call, Employee employee) {
        this(Option.of(call).map(Call::getClientName).getOrElse(() -> null), employee);
    }

    public String getClientName() {
        return clientName;
    }

    @Override
    public String toString() {
        return "Call{" +
                "clientName='" + clientName + '\'' +
                ", attendedBy=" + attendedBy +
                '}';
    }

    public Employee getAttendedBy() {
        return attendedBy;
    }
}
