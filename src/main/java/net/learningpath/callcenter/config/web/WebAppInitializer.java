package net.learningpath.callcenter.config.web;

import net.learningpath.callcenter.config.root.CallCenterControllerConfig;
import net.learningpath.callcenter.config.root.RootContext;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{RootContext.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{CallCenterControllerConfig.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

}
