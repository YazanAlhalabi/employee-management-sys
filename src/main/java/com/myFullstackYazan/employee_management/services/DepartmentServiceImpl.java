package com.myFullstackYazan.employee_management.services;

import com.myFullstackYazan.employee_management.abstracts.DepartmentService;
import com.myFullstackYazan.employee_management.dtos.DepartmentCreate;
import com.myFullstackYazan.employee_management.entities.Department;
import com.myFullstackYazan.employee_management.repositpories.DepartmentRepo;
import com.myFullstackYazan.employee_management.shared.CustomResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DepartmentServiceImpl implements DepartmentService {

  @Autowired
  private DepartmentRepo departmentRepo;

  public Department findOne(UUID departmentId) {
    Department department = departmentRepo.findById(departmentId)
        .orElseThrow(() -> CustomResponseException.ResourceNotFound(
            "Department with id " + departmentId + " not found"
        ));

    return department;
  }

  public List<Department> findAll() {
    return departmentRepo.findAll();
  }

  public Department createOne(DepartmentCreate departmentCreate) {
    Department department = new Department();
    department.setName(departmentCreate.name());

    departmentRepo.save(department);
    return department;
  }

  public void deleteOne(UUID departmentId) {
    departmentRepo.deleteById(departmentId);
  }
}
