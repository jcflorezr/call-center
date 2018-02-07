package net.learningpath.callcenter.employee;

public class Supervisor implements Employee {

    private String greeting;

    public Supervisor() {
        this.greeting = "Hi, is not there any operator available???";
    }

    @Override
    public String answerCall() {
        return greeting;
    }

}
