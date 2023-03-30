package com.secureme.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import reactor.core.publisher.Mono;

@Configuration
public class RouteLocatorConfiguration {

	private static final Logger log= LoggerFactory.getLogger(RouteLocatorConfiguration.class);
	
	@Bean
	@Order(-1)
	GlobalFilter globalFilter() {
	    return (exchange, chain) -> {
	        log.info("first pre filter");
	        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
	            log.info("third post filter");
	        }));
	    };
	}
	
	
}
