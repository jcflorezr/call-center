package net.learningpath.callcenter.employee.factory;

import net.learningpath.callcenter.employee.Operator;

public class OperatorsLevel extends EmployeesLevel {

    private OperatorsLevel(EmployeesLevel nextHierarchyLevel, int numOfAvailableEmployees) {
        super(nextHierarchyLevel, numOfAvailableEmployees, OperatorsLevel.class, Operator.class);
    }

    public static OperatorsLevel newHierarchyLevel(EmployeesLevel nextHierarchyLevel, int numOfAvailableEmployees) {
        return new OperatorsLevel(nextHierarchyLevel, numOfAvailableEmployees);
    }

}
