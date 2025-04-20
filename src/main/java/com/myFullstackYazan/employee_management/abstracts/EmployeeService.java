package com.myFullstackYazan.employee_management.abstracts;

import com.myFullstackYazan.employee_management.dtos.EmployeeCreate;
import com.myFullstackYazan.employee_management.dtos.EmployeeUpdate;
import com.myFullstackYazan.employee_management.entities.Employee;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface EmployeeService {
  Employee findOne(UUID employeeId);

  Page<Employee> findAll(int page, int size);

  void deleteOne(UUID employeeId);

  Employee updateOne(UUID employeeId, EmployeeUpdate employee);

  Employee createOne(EmployeeCreate employee);
}
