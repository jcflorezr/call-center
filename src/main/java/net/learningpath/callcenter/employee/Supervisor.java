package net.learningpath.callcenter.employee;

import java.util.Optional;

public class Supervisor implements Employee {

    private Optional<Employee> boss;
    private String greeting;

    public Supervisor(Employee boss) {
        this.boss = (boss == null) ? Optional.empty() : Optional.of(boss);
        this.greeting = "Hi, is not there any operator available???";
    }

    @Override
    public String answerCall() {
        return greeting;
    }

    @Override
    public Optional<Employee> getBoss() {
        return boss;
    }

}
