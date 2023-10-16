package com.ovd.gestionstock.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.Collections;

@Configuration
public class JasperConfig {

    @Bean
    public ViewResolver getJasperReportsViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/jasper/");
        resolver.setSuffix(".jrxml");
        return resolver;
    }
}
