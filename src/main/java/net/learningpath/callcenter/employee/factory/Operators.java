package net.learningpath.callcenter.employee.factory;

import net.learningpath.callcenter.employee.Employee;
import net.learningpath.callcenter.employee.Operator;

import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Operators implements Employees {

    private Operators() {}

    private static class OperatorsSingleton {
        private static final Operators INSTANCE = new Operators();
    }

    public static Operators getInstance() {
        return Operators.OperatorsSingleton.INSTANCE;
    }

    @Override
    public BlockingQueue<Operator> generateEmployees(Employee boss, int numOfAvailableEmployees) {
        checkBoss(boss);
        return initializeDirectors(boss, numOfAvailableEmployees);
    }

    private void checkBoss(Employee boss) {
        Optional.of(boss instanceof Operator)
                .orElseThrow(() -> new RuntimeException("Employee cannot have a boss of the same level"));
    }

    private BlockingQueue<Operator> initializeDirectors(Employee boss, int numOfAvailableEmployees) {
        return IntStream.range(0, numOfAvailableEmployees)
                .mapToObj(i -> new Operator(boss))
                .collect(Collectors.toCollection(() -> new ArrayBlockingQueue(numOfAvailableEmployees)));
    }

}
