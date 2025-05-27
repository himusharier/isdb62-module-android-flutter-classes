package com.example.project_with_database.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.project_with_database.model.Student;
import com.example.project_with_database.repository.StudentRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StudentService {

	private final StudentRepository repository;

	public StudentService(StudentRepository repository) {
		this.repository = repository;
	}

	public Student saveStudent(Student student) {
		Student saved = repository.save(student);
		return saved;
	}

	public List<Student> getStudents() {
		return repository.findAll();
	}

	public void deleteById(int id) {
		repository.deleteById(id);
	}

	public Optional<Student> findStudentById(int id) {
		return repository.findById(id);
	}

	public List<Student> getStudentsByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

}
