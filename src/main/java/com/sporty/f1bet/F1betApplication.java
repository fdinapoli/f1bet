package com.sporty.f1bet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.sporty.f1bet.service")
public class F1betApplication {

	public static void main(String[] args) {
		SpringApplication.run(F1betApplication.class, args);
	}

}
