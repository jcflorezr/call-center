package net.learningpath.callcenter.employee.hierarchylevel;

import net.learningpath.callcenter.employee.Director;

public class DirectorsLevel extends EmployeesLevel {

    private DirectorsLevel(EmployeesLevel nextHierarchyLevel, int numOfAvailableDirectors) {
        super(nextHierarchyLevel, numOfAvailableDirectors, DirectorsLevel.class, Director.class);
    }

    public static DirectorsLevel newHierarchyLevel(EmployeesLevel nextHierarchyLevel, int numOfAvailableDirectors) {
        return new DirectorsLevel(nextHierarchyLevel, numOfAvailableDirectors);
    }

}
