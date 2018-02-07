package net.learningpath.callcenter.employee;

import java.util.Optional;

public class Director implements Employee {

    private String greeting;

    public Director() {
        this.greeting = "Hi, director speaking...";
    }

    @Override
    public String answerCall() {
        return greeting;
    }

}
