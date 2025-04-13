package com.myFullstackYazan.employee_management.abstracts;

import com.myFullstackYazan.employee_management.dtos.DepartmentCreate;
import com.myFullstackYazan.employee_management.entities.Department;

import java.util.List;
import java.util.UUID;

public interface DepartmentService {
  Department findOne(UUID departmentId);

  List<Department> findAll();

  Department createOne(DepartmentCreate department);

  void deleteOne(UUID departmentId);
}
