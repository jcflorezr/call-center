package net.learningpath.callcenter.employee;

import net.learningpath.callcenter.dto.Call;

import java.util.concurrent.ThreadLocalRandom;

public abstract class Employee {

    protected String greeting;

    public boolean receiveCall(Call call) throws InterruptedException {
        System.out.println(greeting);
        long callDuration = ThreadLocalRandom.current().nextInt(5, 11) * 1000;
        Thread.sleep(callDuration);
        return true;
    }

}
