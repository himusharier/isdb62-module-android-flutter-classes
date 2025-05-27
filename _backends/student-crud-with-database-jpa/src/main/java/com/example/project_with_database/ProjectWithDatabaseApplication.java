package com.example.project_with_database;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy
@SpringBootApplication
public class ProjectWithDatabaseApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectWithDatabaseApplication.class, args);
	}

}
