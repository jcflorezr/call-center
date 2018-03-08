package net.learningpath.callcenter.config.root;

import net.learningpath.callcenter.controller.CallCenterController;
import net.learningpath.callcenter.controller.CallCenterControllerImpl;
import net.learningpath.callcenter.employee.hierarchylevel.EmployeesLevel;
import net.learningpath.callcenter.event.EmployeesAvailabilityTopic;
import net.learningpath.callcenter.service.Dispatcher;
import net.learningpath.callcenter.service.DispatcherImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;

@Configuration
@Import({EmployeesHierarchyConfig.class, EmployeesAvailabilityConfig.class})
public class CallCenterControllerConfig {

    @Autowired
    private EmployeesHierarchyConfig employeesHierarchyConfig;

    @Autowired
    private EmployeesAvailabilityConfig employeesAvailabilityConfig;

    @Bean
    public CallCenterController controllerWithStandardEmployeesHierarchy() {
        EmployeesAvailabilityTopic employeesAvailability = employeesAvailabilityConfig.employeesAvailability();
        EmployeesLevel standardEmployeesHierarchy = employeesHierarchyConfig.standardEmployeesHierarchy();
        Dispatcher dispatcherPerRequest = dispatcher(standardEmployeesHierarchy, employeesAvailability);
        return new CallCenterControllerImpl(standardEmployeesHierarchy, employeesAvailability, dispatcherPerRequest);
    }

    @Bean
    public Dispatcher dispatcher(EmployeesLevel standardEmployeesHierarchy, EmployeesAvailabilityTopic employeesAvailability) {
        return new DispatcherImpl(standardEmployeesHierarchy, employeesAvailability);
    }

}
