package net.learningpath.callcenter.employee;

import io.vavr.control.Option;
import net.learningpath.callcenter.dto.request.Call;
import net.learningpath.callcenter.exceptions.HierarchyLevelException;

import java.util.concurrent.ThreadLocalRandom;

public abstract class Employee {

    protected String greeting;

    public Employee receiveCall(Call call) {
        return Option.of(call)
                .peek(currentCall -> System.out.println("attending call of: " + call.getClientName() + "... " + greeting))
                .map(currentCall -> (long) (ThreadLocalRandom.current().nextInt(5, 11) * 1000))
                .toTry()
                .andThenTry(Thread::sleep).onFailure(HierarchyLevelException::failedWhileAttendingCall)
                .map(time -> call)
                .peek(currentCall -> currentCall.setAttendedBy(this))
                .transform(currentCall -> this);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" +
                "greeting='" + greeting + '\'' +
                '}';
    }
}
