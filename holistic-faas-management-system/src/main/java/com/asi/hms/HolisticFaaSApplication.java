package com.asi.hms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class HolisticFaaSApplication {

	public static void main(String[] args) {
		SpringApplication.run(HolisticFaaSApplication.class, args);
	}

}
