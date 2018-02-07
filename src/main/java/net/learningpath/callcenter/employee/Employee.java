package net.learningpath.callcenter.employee;

import java.util.Optional;

public interface Employee {

    String answerCall();

    Optional<Employee> getBoss();

}
