package net.learningpath.callcenter.employee;

import net.learningpath.callcenter.dto.Call;

import java.util.concurrent.ThreadLocalRandom;

public abstract class Employee {

    protected String greeting;

    public boolean receiveCall(Call call) {
        System.out.println(greeting);
        return processCall();
    }

    private boolean processCall() {
        long callDuration = ThreadLocalRandom.current().nextInt(5, 11) * 1000;
        try {
            Thread.sleep(callDuration);
            return true;
        } catch (InterruptedException e) {
            throw new RuntimeException("Call was lost while process: " + e);
        }
    }

}
