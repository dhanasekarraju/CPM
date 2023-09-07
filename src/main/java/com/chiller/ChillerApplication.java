package com.chiller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages = {"com.chiller"})
public class ChillerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChillerApplication.class, args);
	}

}
