package com.example.project_with_database.model;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentDto {
	private String name;
	private String clazz;
	private int age;
	private String address;
	private LocalDate dob;
}
