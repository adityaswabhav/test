package com.aurionpro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication(scanBasePackages = "com.aurionpro") 

@ComponentScan(basePackages = "com.aurionpro", 
               excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, 
                                                        pattern = "com\\.aurionpro\\.app\\..*"))
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
