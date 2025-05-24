package com.example.project_with_database.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.project_with_database.model.Employee;
import com.example.project_with_database.repository.EmployeeRepository;

@Service
public class EmployeeService {

	private final EmployeeRepository repository;

	public EmployeeService(EmployeeRepository repository) {
		this.repository = repository;
	}

	public Employee saveEmployee(Employee employee) {
		int save = repository.save(employee);
		return getEmpById(save);
	}

	public Employee getEmpById(int id) {
		Optional<Employee> byId = repository.findById(id);
		return byId.get();
	}

	public List<Employee> getAllEmp() {
		List<Employee> all = repository.findAll();
		return all;
	}

	public int DelEmpById(int id) {
		return repository.deleteById(id);
	}

	public Employee updateEmp(int id, Employee employee) {
		employee.setId(id);
		repository.update(employee);
		return getEmpById(id);
	}

	public List<Employee> getEmpByName(String name) {
		return repository.getEmployeeByName(name);
	}

}
