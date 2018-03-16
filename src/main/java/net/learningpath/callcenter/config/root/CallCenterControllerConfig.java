package net.learningpath.callcenter.config.root;

import net.learningpath.callcenter.employee.hierarchylevel.EmployeesLevel;
import net.learningpath.callcenter.event.EmployeesAvailabilityTopic;
import net.learningpath.callcenter.service.Dispatcher;
import net.learningpath.callcenter.service.DispatcherImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({EmployeesHierarchyConfig.class, EmployeesAvailabilityConfig.class})
public class CallCenterControllerConfig {

    @Autowired
    private EmployeesHierarchyConfig employeesHierarchyConfig;

    @Autowired
    private EmployeesAvailabilityConfig employeesAvailabilityConfig;

    @Bean
    public Dispatcher dispatcher() {
        EmployeesAvailabilityTopic employeesAvailability = employeesAvailabilityConfig.employeesAvailability();
        EmployeesLevel standardEmployeesHierarchy = employeesHierarchyConfig.standardEmployeesHierarchy();
        return new DispatcherImpl(standardEmployeesHierarchy, employeesAvailability);
    }

}
