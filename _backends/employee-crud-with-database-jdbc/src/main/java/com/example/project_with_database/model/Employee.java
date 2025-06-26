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
public class Employee {

	private int id;
	private String name;
	private String email;
	private String designation;
	private int age;
	private String address;
	private LocalDate dob;
	private double salary;
	private String image;
}
