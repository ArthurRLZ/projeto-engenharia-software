package br.edu.ufape.backend.config;

import org.h2.server.web.JakartaWebServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
 * configura o h2 manualmente pq o spring boot 4 removeu a autoconf
 * URL: jdbc:h2:mem:backenddb | User: sa | Password: (vazio)
 */
@Configuration
public class H2ConsoleConfig {

    @Bean
    public ServletRegistrationBean<JakartaWebServlet> h2ConsoleServlet() {
        JakartaWebServlet servlet = new JakartaWebServlet();
        ServletRegistrationBean<JakartaWebServlet> bean =
                new ServletRegistrationBean<>(servlet, "/h2-console", "/h2-console/*");
        bean.addInitParameter("webAllowOthers", "false");
        bean.setLoadOnStartup(1);
        return bean;
    }
}
