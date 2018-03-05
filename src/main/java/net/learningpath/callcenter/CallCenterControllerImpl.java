package net.learningpath.callcenter;

import net.learningpath.callcenter.dto.request.Call;
import net.learningpath.callcenter.dto.response.Response;
import net.learningpath.callcenter.employee.hierarchylevel.EmployeesLevel;
import net.learningpath.callcenter.event.EmployeesAvailabilityTopic;
import net.learningpath.callcenter.service.Dispatcher;
import net.learningpath.callcenter.service.DispatcherImpl;
import org.springframework.stereotype.Controller;

@Controller
public class CallCenterControllerImpl implements CallCenterController {

    private final EmployeesLevel employeesLevel;
    private final EmployeesAvailabilityTopic employeesAvailability;

    public CallCenterControllerImpl(EmployeesLevel employeesLevel, EmployeesAvailabilityTopic employeesAvailability) {
        this.employeesLevel = employeesLevel;
        this.employeesAvailability = employeesAvailability;
    }

    @Override
    public Response getRequest(Call call) {
        Dispatcher dispatcher = new DispatcherImpl(employeesAvailability, employeesLevel);
        return dispatcher.dispatchCall(call);
    }

}
