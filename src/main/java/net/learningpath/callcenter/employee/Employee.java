package net.learningpath.callcenter.employee;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.vavr.control.Option;
import net.learningpath.callcenter.dto.request.Call;
import net.learningpath.callcenter.exceptions.HierarchyLevelException;

import java.util.concurrent.ThreadLocalRandom;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Director.class, name = "Director"),
        @JsonSubTypes.Type(value = Supervisor.class, name = "Supervisor"),
        @JsonSubTypes.Type(value = Operator.class, name = "Operator")}
)
public abstract class Employee {

    protected String greeting;

    public Employee receiveCall(Call call) {
        return Option.of(call)
                .peek(currentCall -> System.out.println("attending call of: " + call.getClientName() + "... " + greeting))
                .map(currentCall -> (long) (ThreadLocalRandom.current().nextInt(5, 11) * 1000))
                .toTry()
                .andThenTry(Thread::sleep).onFailure(HierarchyLevelException::failedWhileAttendingCall)
                .map(time -> call)
                .transform(currentCall -> this);
    }

    public String getGreeting() {
        return greeting;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" +
                "greeting='" + greeting + '\'' +
                '}';
    }
}
