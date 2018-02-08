package net.learningpath.callcenter.employee.factory;

import net.learningpath.callcenter.employee.Director;

public class DirectorsLevel extends EmployeesLevel {

    private DirectorsLevel(EmployeesLevel nextHierarchyLevel, int numOfAvailableEmployees) {
        super(nextHierarchyLevel, numOfAvailableEmployees, DirectorsLevel.class, Director.class);
    }

    public static DirectorsLevel newHierarchyLevel(EmployeesLevel nextHierarchyLevel, int numOfAvailableEmployees) {
        return new DirectorsLevel(nextHierarchyLevel, numOfAvailableEmployees);
    }

}
