package net.learningpath.callcenter.dto.employee;

import java.util.Optional;

public interface Employee {

    String answerCall();

    Optional<Employee> getBoss();

}
