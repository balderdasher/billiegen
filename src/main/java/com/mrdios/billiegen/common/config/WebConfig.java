package com.mrdios.billiegen.common.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author CodePorter
 * @date 2017-10-24
 */
@Configuration
public class WebConfig extends BaseConfig {

    private final Logger logger = LogManager.getLogger();

    /**
     * open session in view filter
     *
     * @return OpenEntityManagerInViewFilter
     */
    @Bean
    public FilterRegistrationBean openSessionInViewFilter() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean(new OpenEntityManagerInViewFilter());
        registrationBean.addUrlPatterns("/");
        Map<String, String> initParams = new HashMap<>();
        initParams.put("excludeSuffixs", "js,css,jpg,jpeg,gif,png,bmp,html");
        initParams.put("singleSession", "true");
        registrationBean.setInitParameters(initParams);
        return registrationBean;
    }

//    @Bean
//    public ServletRegistrationBean backServlet() {
//        AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
//        applicationContext.register(BackConfig.class);
//        DispatcherServlet backServlet = new DispatcherServlet(applicationContext);
//        ServletRegistrationBean registrationBean = new ServletRegistrationBean(backServlet);
//        registrationBean.setLoadOnStartup(1);
//        registrationBean.addUrlMappings("/back/*");
//        registrationBean.setName("backServlet");
//        return registrationBean;
//    }
//
//    @Bean
//    public ServletRegistrationBean frontServlet() {
//        AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
//        applicationContext.register(FrontConfig.class);
//        DispatcherServlet frontServlet = new DispatcherServlet(applicationContext);
//        ServletRegistrationBean registrationBean = new ServletRegistrationBean(frontServlet);
//        registrationBean.setLoadOnStartup(2);
//        registrationBean.addUrlMappings("/front/*");
//        registrationBean.setName("frontServlet");
//        return registrationBean;
//    }

}
