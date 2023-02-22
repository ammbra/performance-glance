package org.acme.example.jfr;

import jakarta.servlet.ServletContextListener;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JFRConfig {

	@Bean
	public FilterRegistrationBean<FlightRecorderFilter> jfrFilter() {
		FilterRegistrationBean<FlightRecorderFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new FlightRecorderFilter());
		registrationBean.addUrlPatterns("/api/todo/*");
		return registrationBean;

	}

	@Bean
	ServletListenerRegistrationBean<ServletContextListener> servletListener() {
		ServletListenerRegistrationBean<ServletContextListener> srb
				= new ServletListenerRegistrationBean<>();
		srb.setListener(new MetricsListener());
		return srb;
	}

}