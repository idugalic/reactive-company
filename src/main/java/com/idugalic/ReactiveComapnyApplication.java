package com.idugalic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@EnableReactiveMongoRepositories
@EnableWebFlux
public class ReactiveComapnyApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReactiveComapnyApplication.class, args);
	}
}
