package com.qianyi.sportstrainingassistant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
@SpringBootApplication
@EnableScheduling
public class SportsTrainingAssistantApplication {

	public static void main(String[] args) {
		SpringApplication.run(SportsTrainingAssistantApplication.class, args);
	}

}
