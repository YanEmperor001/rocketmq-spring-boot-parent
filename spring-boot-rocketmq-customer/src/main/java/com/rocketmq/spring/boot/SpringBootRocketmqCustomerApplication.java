package com.rocketmq.spring.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.rocketmq.spring.boot.listener"})
public class SpringBootRocketmqCustomerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootRocketmqCustomerApplication.class, args);
	}
}
