package net.learningpath.callcenter.employee.factory;

import net.learningpath.callcenter.employee.Employee;
import net.learningpath.callcenter.employee.Supervisor;

import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Supervisors implements Employees {

    private Supervisors() {}

    private static class SupervisorsSingleton {
        private static final Supervisors INSTANCE = new Supervisors();
    }

    public static Supervisors getInstance() {
        return SupervisorsSingleton.INSTANCE;
    }

    @Override
    public BlockingQueue<Supervisor> generateEmployees(Employee boss, int numOfAvailableEmployees) {
        checkBoss(boss);
        return initializeSupervisors(boss, numOfAvailableEmployees);
    }

    private void checkBoss(Employee boss) {
        Optional.of(boss instanceof Supervisor)
                .orElseThrow(() -> new RuntimeException("Employee cannot have a boss of the same level"));
    }

    private BlockingQueue<Supervisor> initializeSupervisors(Employee boss, int numOfAvailableEmployees) {
        return IntStream.range(0, numOfAvailableEmployees)
                .mapToObj(i -> new Supervisor(boss))
                .collect(Collectors.toCollection(() -> new ArrayBlockingQueue(numOfAvailableEmployees)));
    }

}
