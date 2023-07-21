package com.first.firstmicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class FirstmicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FirstmicroserviceApplication.class, args);
	}

}
