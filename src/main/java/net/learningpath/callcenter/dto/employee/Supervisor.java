package net.learningpath.callcenter.dto.employee;

import java.util.Optional;

public class Supervisor implements Employee {

    private Optional<Employee> boss;

    public Supervisor(Employee boss) {
        if (boss instanceof Director) {
            throw new RuntimeException("Employee cannot have a boss of same level");
        }
        this.boss = (boss == null) ? Optional.empty() : Optional.of(boss);
    }

    @Override
    public String answerCall() {
        return "Hi, supervisor speaking...";
    }

    @Override
    public Optional<Employee> getBoss() {
        return boss;
    }

}
