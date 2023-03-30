package com.secureme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SecureMeGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecureMeGatewayApplication.class, args);
	}
}
