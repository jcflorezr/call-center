package net.learningpath.callcenter.config;

import net.learningpath.callcenter.controller.CallCenterController;
import net.learningpath.callcenter.controller.CallCenterControllerImpl;
import net.learningpath.callcenter.employee.hierarchylevel.EmployeesLevel;
import net.learningpath.callcenter.employee.hierarchylevel.OperatorsLevel;
import net.learningpath.callcenter.event.EmployeesAvailability;
import net.learningpath.callcenter.event.EmployeesAvailabilityTopic;
import net.learningpath.callcenter.service.Dispatcher;
import net.learningpath.callcenter.service.DispatcherImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;

@Configuration
public class TestRootContext {

    @Bean
    public CallCenterController controllerWithStandardEmployeesHierarchy() {
        EmployeesAvailabilityTopic employeesAvailability = mock(EmployeesAvailability.class);
        EmployeesLevel standardEmployeesHierarchy = mock(OperatorsLevel.class);
        Dispatcher dispatcherPerRequest = dispatcher();
        return new CallCenterControllerImpl(standardEmployeesHierarchy, employeesAvailability, dispatcherPerRequest);
    }

    @Bean
    public Dispatcher dispatcher() {
        return mock(DispatcherImpl.class);
    }

}
