package net.learningpath.callcenter.employee;


public class Operator implements Employee {

    private String greeting;

    public Operator() {
        this.greeting = "Hi!, how can I help you?";
    }

    @Override
    public String answerCall() {
        return greeting;
    }

}
