package net.learningpath.callcenter.employee;

import java.util.Optional;

public class Director implements Employee {

    private Optional<Employee> boss;
    private String greeting;

    public Director(Employee boss) {
        this.boss = (boss == null) ? Optional.empty() : Optional.of(boss);
        this.greeting = "Hi, director speaking...";
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
