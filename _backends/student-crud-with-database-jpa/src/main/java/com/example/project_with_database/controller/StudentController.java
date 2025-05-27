package com.example.project_with_database.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.project_with_database.model.Student;
import com.example.project_with_database.service.StudentService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/student")
public class StudentController {
	// field injection
	// @Autowired

	// private static final Logger log =
	// LoggerFactory.getLogger(StudentController.class);

//	private String msg = "new message";
	@Value("${custom.msg}")
	private String msg;

	private final StudentService service;

	// constructor injection
	public StudentController(StudentService service) {
		this.service = service;
	}

	@PostMapping
	public Student saveStudent(@RequestBody Student student) {
		Student saveStudent = service.saveStudent(student);
		return saveStudent;
	}

	@GetMapping
	public List<Student> getStudents() {
		log.info("calling get Students");
		return service.getStudents();
	}

	@DeleteMapping("/{id}")
	public void deleteById(@PathVariable int id) {
		service.deleteById(id);
	}

	@PutMapping("/{id}")
	public Student updateStudent(@PathVariable int id, @RequestBody Student student) {
		Optional<Student> existingStudent = service.findStudentById(id);
		Student st = null;
		if (existingStudent.isPresent()) {
			st = existingStudent.get();

			if (st.getName() != student.getName()) {
				st.setName(student.getName());
			}
			if (st.getAddress() != student.getAddress()) {
				st.setAddress(student.getAddress());
			}
			if (st.getAge() != student.getAge()) {
				st.setAge(student.getAge());
			}
			if (st.getClazz() != student.getClazz()) {
				st.setClazz(student.getClazz());
			}
			if (st.getDob() != student.getDob()) {
				st.setDob(student.getDob());
			}

		}
		return service.saveStudent(st);
	}

	@GetMapping("/byName")
	public List<Student> getStudentByName(@RequestParam String name) {
		List<Student> students = service.getStudentsByName(name);
		return students;
	}

	@GetMapping("/hello")
	public String hello() {
		return "hello, students! " + msg;
	}

}
