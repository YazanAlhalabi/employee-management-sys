package com.myFullstackYazan.employee_management.services;

import com.myFullstackYazan.employee_management.abstracts.EmployeeService;
import com.myFullstackYazan.employee_management.dtos.EmployeeCreate;
import com.myFullstackYazan.employee_management.dtos.EmployeeUpdate;
import com.myFullstackYazan.employee_management.entities.Department;
import com.myFullstackYazan.employee_management.entities.Employee;
import com.myFullstackYazan.employee_management.repositpories.DepartmentRepo;
import com.myFullstackYazan.employee_management.repositpories.EmployeeRepo;
import com.myFullstackYazan.employee_management.shared.CustomResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class EmployeeServiceImpl implements EmployeeService {
  @Autowired
  private EmployeeRepo employeeRepo;

  @Autowired
  private DepartmentRepo departmentRepo;

  @Autowired
  private EmailService emailService;

  @PreAuthorize("@securityUtils.isOwner(#employeeId)")
  public Employee findOne(UUID employeeId) {
    Employee employee = employeeRepo.findById(employeeId)
        .orElseThrow(() -> CustomResponseException.ResourceNotFound(
            "Employee with id " + employeeId + " not found"
        ));

    return employee;
  }

  public Page<Employee> findAll(int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    return employeeRepo.findAll(pageable);
  }

  public void deleteOne(UUID employeeId) {
    Optional<Employee> employee = employeeRepo.findById(employeeId);

    employee.ifPresent(value -> employeeRepo.deleteById(value.getId()));
  }

  @PreAuthorize("@securityUtils.isOwner(#employeeId)")
  public Employee updateOne(UUID employeeId, EmployeeUpdate employee) {
    Employee existingEmployee = employeeRepo.findById(employeeId)
        .orElseThrow(() -> CustomResponseException.ResourceNotFound(
            "Employee with id " + employeeId + " not found"
        ));


    existingEmployee.setFirstName(employee.firstName());
    existingEmployee.setLastName(employee.lastName());
    existingEmployee.setPhoneNumber(employee.phoneNumber());
    existingEmployee.setPosition(employee.position());

    employeeRepo.save(existingEmployee);

    return existingEmployee;
  }

  @Transactional
  public Employee createOne(EmployeeCreate employeeCreate) {
    Employee employee = new Employee();

    Department department = departmentRepo.findById(employeeCreate.departmentId())
        .orElseThrow(() -> CustomResponseException.ResourceNotFound(
            "Department with id " + employeeCreate.departmentId() + " not found"
        ));

    String token = UUID.randomUUID().toString();

    try {
      employee.setVerified(false);
      employee.setAccountCreationToken(token);

      employee.setFirstName(employeeCreate.firstName());
      employee.setLastName(employeeCreate.lastName());
      employee.setPosition(employeeCreate.position());
      employee.setHireDate(employeeCreate.hireDate());
      employee.setPhoneNumber(employeeCreate.phoneNumber());
      employee.setEmail(employeeCreate.email());
      employee.setDepartment(department);

      employeeRepo.save(employee);

      emailService.sendAccountCreationEmail(employee.getEmail(), token);

      return employee;
    } catch (Exception e) {
      throw CustomResponseException.BadRequest("Employee creation failed");
    }
  }
}
