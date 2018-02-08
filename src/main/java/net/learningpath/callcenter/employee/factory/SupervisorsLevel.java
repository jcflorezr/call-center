package net.learningpath.callcenter.employee.factory;

import net.learningpath.callcenter.employee.Supervisor;

public class SupervisorsLevel extends EmployeesLevel {

    private SupervisorsLevel(EmployeesLevel nextHierarchyLevel, int numOfAvailableEmployees) {
        super(nextHierarchyLevel, numOfAvailableEmployees, SupervisorsLevel.class, Supervisor.class);
    }

    public static SupervisorsLevel newHierarchyLevel(EmployeesLevel nextHierarchyLevel, int numOfAvailableEmployees) {
        return new SupervisorsLevel(nextHierarchyLevel, numOfAvailableEmployees);
    }

}
