package com.example.inventorymanagementsystem;
//import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
@SpringBootApplication
public class InventoryManagementSystemApplication {
	
//	@Bean
//	public CommandLineRunner runner(ApplicationContext context) {
//	    return args -> {
//	        System.out.println("---- Loaded Controllers ----");
//	        String[] beanNames = context.getBeanDefinitionNames();
//	        for (String name : beanNames) {
//	            if (name.toLowerCase().contains("controller")) {
//	                System.out.println(name);
//	            }
//	        }
//	        System.out.println("----------------------------");
//	    };
//	}

		public static void main(String[] args) {
			SpringApplication.run(InventoryManagementSystemApplication.class, args);
		}

	}



