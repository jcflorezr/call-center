package net.learningpath.callcenter.employee.factory;

import net.learningpath.callcenter.employee.Director;
import net.learningpath.callcenter.employee.Employee;

import java.util.concurrent.BlockingQueue;

public interface Employees {

    BlockingQueue<? extends Employee> generateEmployees(Employee boss, int numOfAvailableEmployees);
}
