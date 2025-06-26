package com.example.project_with_database.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.example.project_with_database.service.FileStorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.project_with_database.model.Employee;
import com.example.project_with_database.service.EmployeeService;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

//@Controller
@RestController
@RequestMapping(value = "/employee")
public class EmployeeController {

	private final EmployeeService service;
	private final FileStorageService storageService;

	public EmployeeController(EmployeeService service, FileStorageService storageService) {
		this.service = service;
		this.storageService = storageService;
	}

//	@RequestMapping(method = RequestMethod.POST)
	@PostMapping
	public Employee saveEmp(@RequestBody Employee employee) {
		Employee savedEmp = service.saveEmployee(employee);
		return savedEmp;
	}

	@GetMapping("/{id}")
	public Employee getEmpById(@PathVariable int id) {
		Employee empById = service.getEmpById(id);
		return empById;
	}

	@GetMapping
	public List<Employee> getAllEmp() {
		List<Employee> allEmp = service.getAllEmp();
		return allEmp;
	}

	@DeleteMapping("/{id}")
	public String DelEmpById(@PathVariable int id) {
		int empById = service.DelEmpById(id);
		if (empById > 0) {
			return "Deleted!";
		} else {
			return "Error!";
		}
	}

	@PutMapping("/{id}")
	public Employee updateEmp(@PathVariable int id, @RequestBody Employee employee) {
		Employee update = service.updateEmp(id, employee);
		return update;
	}

	@GetMapping("find/{name}")
	public List<Employee> findByName(@PathVariable String name) {
		return service.getEmpByName(name);
	}

	@PostMapping("/{employeeId}/upload")
	public ResponseEntity<?> uploadFile(@PathVariable Integer employeeId,
										@RequestParam("file") MultipartFile file) {
		try {
			String savedFileName = storageService.storeFile(file);
			String accessUrl = ServletUriComponentsBuilder
					.fromCurrentContextPath()
					.path("/imageUrl/")
					.path(savedFileName)
					.toUriString();

			Employee empById = service.getEmpById(employeeId);
			empById.setImage(accessUrl);

			service.updateEmp(employeeId, empById);

			return ResponseEntity.ok(Map.of("message", "File uploaded", "url", accessUrl));

		} catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("error", e.getMessage()));
        }
    }

}
