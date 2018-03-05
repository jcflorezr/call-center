package net.learningpath.callcenter.config;

import net.learningpath.callcenter.event.EmployeesAvailability;
import net.learningpath.callcenter.event.EmployeesAvailabilityTopic;
import net.learningpath.callcenter.service.Dispatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Configuration
public class EmployeesAvailabilityConfig {

    @Bean
    public EmployeesAvailabilityTopic employeesAvailability() {
        return new EmployeesAvailability(linkedBlockingQueueOfDispatchers());
    }

    @Bean
    public BlockingQueue<Dispatcher> linkedBlockingQueueOfDispatchers() {
        return new LinkedBlockingQueue<>();
    }

}
