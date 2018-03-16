package net.learningpath.callcenter.config.root;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({CallCenterControllerConfig.class})
public class RootContext {
}
