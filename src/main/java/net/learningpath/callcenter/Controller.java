package net.learningpath.callcenter;

import net.learningpath.callcenter.dto.Call;
import net.learningpath.callcenter.employee.factory.DirectorsLevel;
import net.learningpath.callcenter.employee.factory.EmployeesLevel;
import net.learningpath.callcenter.employee.factory.OperatorsLevel;
import net.learningpath.callcenter.employee.factory.SupervisorsLevel;
import net.learningpath.callcenter.event.topic.EmployeesAvailability;
import net.learningpath.callcenter.event.topic.Topic;
import net.learningpath.callcenter.service.Dispatcher;
import net.learningpath.callcenter.service.DispatcherImpl;

public class Controller {

    private final EmployeesLevel directorsLevel;
    private final EmployeesLevel supervisorsLevel;
    private final EmployeesLevel operatorsLevel;
    private final int numOfDirectors = 1;
    private final int numOfSupervisors = 5;
    private final int numOfOperators = 10;
    private final Dispatcher dispatcher;
    private final Topic employeesAvailability;

    private static class ControllerHolder {
        private static final Controller INSTANCE = new Controller();
    }

    private static Controller getInstance() {
        return ControllerHolder.INSTANCE;
    }

    private Controller() {
        // Chain of Responsibility
        directorsLevel = DirectorsLevel.newHierarchyLevel(null, numOfDirectors);
        supervisorsLevel = SupervisorsLevel.newHierarchyLevel(directorsLevel, numOfSupervisors);
        operatorsLevel = OperatorsLevel.newHierarchyLevel(supervisorsLevel, numOfOperators);
        // Observer
        dispatcher = DispatcherImpl.getInstance();
        employeesAvailability = new EmployeesAvailability();
        employeesAvailability.register(dispatcher);
    }

    public void getRequest(Call call) {
        dispatcher.dispatchCall(call, operatorsLevel, employeesAvailability);
    }


}
