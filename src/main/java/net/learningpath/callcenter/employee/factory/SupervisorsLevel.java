package net.learningpath.callcenter.employee.factory;

import net.learningpath.callcenter.employee.Supervisor;

public class SupervisorsLevel extends EmployeesLevel {

    private SupervisorsLevel(EmployeesLevel nextHierarchyLevel, int numOfAvailableSupervisors) {
        super(nextHierarchyLevel, numOfAvailableSupervisors, SupervisorsLevel.class, Supervisor.class);
    }

    public static SupervisorsLevel newHierarchyLevel(EmployeesLevel nextHierarchyLevel, int numOfAvailableSupervisors) {
        return new SupervisorsLevel(nextHierarchyLevel, numOfAvailableSupervisors);
    }

}
