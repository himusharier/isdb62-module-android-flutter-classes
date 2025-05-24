package com.example.project_with_database.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.example.project_with_database.model.Employee;

@Repository
public class EmployeeRepository {
	private final JdbcTemplate jdbcTemplate;
	private final SimpleJdbcInsert employeeInsert;

	public EmployeeRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;

		this.employeeInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("employees_sb_jdbc")
				.usingGeneratedKeyColumns("id");
	}

	public int save(Employee employee) {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("name", employee.getName());
		parameters.put("email", employee.getEmail());
		parameters.put("designation", employee.getDesignation());
		parameters.put("age", employee.getAge());
		parameters.put("address", employee.getAddress());
		parameters.put("dob", employee.getDob());
		parameters.put("salary", employee.getSalary());

		Number key = employeeInsert.executeAndReturnKey(parameters);
		return key.intValue();
	}

	public Optional<Employee> findById(int id) {
		try {
			String sql = "SELECT * FROM employees_sb_jdbc WHERE id = ?";
			return Optional.ofNullable(jdbcTemplate.queryForObject(sql, new EmployeeRowMapper(), id));

		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}

	private static class EmployeeRowMapper implements RowMapper<Employee> {
		@Override
		public Employee mapRow(ResultSet resultSet, int rowNum) throws SQLException {
			return new Employee(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getString("email"),
					resultSet.getString("designation"), resultSet.getInt("age"), resultSet.getString("address"),
					resultSet.getObject("dob", LocalDate.class), resultSet.getDouble("salary"));
		}
	}

	public List<Employee> findAll() {
		String sql = "SELECT * FROM employees_sb_jdbc";
		return jdbcTemplate.query(sql, new EmployeeRowMapper());
	}

	public int deleteById(int id) {
		String sql = "DELETE FROM employees_sb_jdbc WHERE id = ?";
		return jdbcTemplate.update(sql, id);
	}

	public int update(Employee employee) {
		String sql = "UPDATE employees_sb_jdbc SET name = ?, email = ?, designation = ?, "
				+ "age = ?, address = ?, dob = ?, salary = ? WHERE id = ?";

		return jdbcTemplate.update(sql, employee.getName(), employee.getEmail(), employee.getDesignation(),
				employee.getAge(), employee.getAddress(), employee.getDob(), employee.getSalary(), employee.getId());
	}

	public List<Employee> getEmployeeByName(String name) {
		String sql = "SELECT * FROM employees_sb_jdbc WHERE name LIKE ?";
		return jdbcTemplate.query(sql, new EmployeeRowMapper(), "%" + name + "%");
	}

}
