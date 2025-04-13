package com.myFullstackYazan.employee_management.repositpories;

import com.myFullstackYazan.employee_management.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee, UUID> {
  Optional<Employee> findOneByAccountCreationToken(String token);
}
