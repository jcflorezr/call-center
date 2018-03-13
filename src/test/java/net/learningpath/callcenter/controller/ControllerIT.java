package net.learningpath.callcenter.controller;

import net.learningpath.callcenter.config.EmbeddedTomcatConfig;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@PropertySource("classpath:embedded-tomcat.properties")
@ContextConfiguration(classes = {EmbeddedTomcatConfig.class})
@WebAppConfiguration
public class ControllerIT {

    @Autowired
    private Tomcat tomcat;

    @Test
    public void firstTest() throws LifecycleException {
        tomcat.start();
        tomcat.getServer().await();
    }

}
