package com.example.project_with_database.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.project_with_database.model.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

	List<Student> findAllByName(String name);

	List<Student> findAllByNameAndAge(String name, int age);

}
