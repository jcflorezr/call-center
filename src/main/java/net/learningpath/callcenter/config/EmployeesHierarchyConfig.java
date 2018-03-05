package net.learningpath.callcenter.config;

import net.learningpath.callcenter.employee.hierarchylevel.DirectorsLevel;
import net.learningpath.callcenter.employee.hierarchylevel.EmployeesLevel;
import net.learningpath.callcenter.employee.hierarchylevel.OperatorsLevel;
import net.learningpath.callcenter.employee.hierarchylevel.SupervisorsLevel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:num-of-employees-per-hierarchy-level.properties")
public class EmployeesHierarchyConfig {

    @Value("${directors}")
    private int numOfDirectors;

    @Value("${supervisors}")
    private int numOfSupervisors;

    @Value("${operators}")
    private int numOfOperators;

    @Bean
    public EmployeesLevel standardEmployeesHierarchy() {
        return getStandardOperatorsLevel();
    }

    @Bean
    public EmployeesLevel getStandardDirectorsLevel() {
        return DirectorsLevel.newHierarchyLevel(null, numOfDirectors);
    }

    @Bean
    public EmployeesLevel getStandardSupervisorsLevel() {
        return SupervisorsLevel.newHierarchyLevel(getStandardDirectorsLevel(), numOfSupervisors);
    }

    @Bean
    public EmployeesLevel getStandardOperatorsLevel() {
        return OperatorsLevel.newHierarchyLevel(getStandardSupervisorsLevel(), numOfOperators);
    }

}
