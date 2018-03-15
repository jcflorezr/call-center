package net.learningpath.callcenter.config;

import net.learningpath.callcenter.service.Dispatcher;
import net.learningpath.callcenter.service.DispatcherImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import static org.mockito.Mockito.mock;

@Configuration
public class TestRootContext {

    @Bean
    public Dispatcher dispatcher() {
        return mock(DispatcherImpl.class);
    }

}
