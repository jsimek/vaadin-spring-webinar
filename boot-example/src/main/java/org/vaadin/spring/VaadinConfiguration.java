package org.vaadin.spring;

import org.apache.log4j.Logger;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * TODO support the *Configurer pattern to permit parameterization of things \
 * TODO like the {@link org.vaadin.spring.SpringAwareVaadinServlet registration}.
 *
 * @author petter@vaadin.com
 * @author Josh Long (josh@joshlong.com)
 * @see org.vaadin.spring.EnableVaadin
 */
@Configuration
public class VaadinConfiguration {


    private Logger logger = Logger.getLogger(getClass());

    @Bean
    static VaadinUiScope uiScope() {
        return new VaadinUiScope();
    }


    @Bean
    ServletRegistrationBean vaadinServlet() {
        logger.debug("registering vaadinServlet()");
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(
                new SpringAwareVaadinServlet(), "/*", "/VAADIN/*");
        registrationBean.addInitParameter("heartbeatInterval", "10"); // In order to test that orphaned UIs are detached properly
        return registrationBean;
    }
}
