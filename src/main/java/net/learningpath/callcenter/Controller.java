package net.learningpath.callcenter;

import net.learningpath.callcenter.dto.request.Call;
import net.learningpath.callcenter.dto.response.Response;
import net.learningpath.callcenter.employee.hierarchylevel.DirectorsLevel;
import net.learningpath.callcenter.employee.hierarchylevel.EmployeesLevel;
import net.learningpath.callcenter.employee.hierarchylevel.OperatorsLevel;
import net.learningpath.callcenter.employee.hierarchylevel.SupervisorsLevel;
import net.learningpath.callcenter.event.EmployeesAvailability;
import net.learningpath.callcenter.event.EmployeesAvailabilityTopic;
import net.learningpath.callcenter.service.Dispatcher;
import net.learningpath.callcenter.service.DispatcherImpl;

public class Controller {

    private final EmployeesLevel directorsLevel;
    private final EmployeesLevel supervisorsLevel;
    private final EmployeesLevel operatorsLevel;
    private static final int NUM_OF_DIRECTORS = 5;
    private static final int NUM_OF_SUPERVISORS = 15;
    private static final int NUM_OF_OPERATORS = 80;
    private final EmployeesAvailabilityTopic employeesAvailability;

    private Controller() {
        // Chain of Responsibility
        directorsLevel = DirectorsLevel.newHierarchyLevel(null, NUM_OF_DIRECTORS);
        supervisorsLevel = SupervisorsLevel.newHierarchyLevel(directorsLevel, NUM_OF_SUPERVISORS);
        operatorsLevel = OperatorsLevel.newHierarchyLevel(supervisorsLevel, NUM_OF_OPERATORS);
        // Producer-Consumer
        employeesAvailability = EmployeesAvailability.getInstance();
    }

    private static class ControllerHolder {
        private ControllerHolder() {}
        private static final Controller INSTANCE = new Controller();
    }

    public static Controller getInstance() {
        return ControllerHolder.INSTANCE;
    }

    public Response getRequest(Call call) {
        Dispatcher dispatcher = new DispatcherImpl(call, employeesAvailability, operatorsLevel);
        return dispatcher.dispatchCall();
    }

}
