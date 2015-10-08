package com.kang.SpringWebsocket.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
 
public class HelloWorldInitializer implements WebApplicationInitializer {
 
    public void onStartup(ServletContext container) throws ServletException {
 
        AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
        //ctx.register(HelloWorldConfiguration.class);
        //ctx.setServletContext(container);
        ctx.scan("com.kang.SpringWebsocket");
        
        container.addListener(new ContextLoaderListener(ctx));        
        
        ServletRegistration.Dynamic servlet = container.addServlet("dispatcher", new DispatcherServlet(ctx));
              
        //security filter??
//        FilterRegistration.Dynamic securityFilter = container.addFilter("springSecurityFilterChain", DelegatingFilterProxy.class);
//        securityFilter.addMappingForUrlPatterns(null, false, "/");
        
        servlet.setAsyncSupported(true);
        servlet.setLoadOnStartup(1);
        servlet.addMapping("/");
    }
 
}