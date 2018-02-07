package net.learningpath.callcenter.employee.factory;

import net.learningpath.callcenter.employee.Director;
import net.learningpath.callcenter.employee.Employee;

import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Directors implements Employees {

    private Directors() {}

    private static class DirectorsSingleton {
        private static final Directors INSTANCE = new Directors();
    }

    public static Directors getInstance() {
        return DirectorsSingleton.INSTANCE;
    }

    @Override
    public BlockingQueue<Director> generateEmployees(Employee boss, int numOfAvailableEmployees) {
        checkBoss(boss);
        return initializeDirectors(boss, numOfAvailableEmployees);
    }

    private void checkBoss(Employee boss) {
        Optional.of(boss instanceof Director)
                .orElseThrow(() -> new RuntimeException("Employee cannot have a boss of the same level"));
    }

    private BlockingQueue<Director> initializeDirectors(Employee boss, int numOfAvailableEmployees) {
        return IntStream.range(0, numOfAvailableEmployees)
                .mapToObj(i -> new Director(boss))
                .collect(Collectors.toCollection(() -> new ArrayBlockingQueue(numOfAvailableEmployees)));
    }

}
