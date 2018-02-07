package net.learningpath.callcenter.employee;

import java.util.Optional;

public class Operator implements Employee {

    private Optional<Employee> boss;
    private String greeting;

    public Operator(Employee boss) {
        this.boss = (boss == null) ? Optional.empty() : Optional.of(boss);
        this.greeting = "Hi!, how can I help you?";
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
