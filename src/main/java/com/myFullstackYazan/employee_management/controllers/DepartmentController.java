package com.myFullstackYazan.employee_management.controllers;


import com.myFullstackYazan.employee_management.abstracts.DepartmentService;
import com.myFullstackYazan.employee_management.dtos.DepartmentCreate;
import com.myFullstackYazan.employee_management.entities.Department;
import com.myFullstackYazan.employee_management.shared.GlobalResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/departments")
public class DepartmentController {
  @Autowired
  private DepartmentService departmentService;


  @GetMapping
  public ResponseEntity<GlobalResponse<List<Department>>> findAll() {
    List<Department> departments = departmentService.findAll();
    return new ResponseEntity<>(new GlobalResponse<>(departments), HttpStatus.OK);
  }

  @GetMapping("/{departmentId}")
  public ResponseEntity<GlobalResponse<Department>> findOne(@PathVariable UUID departmentId) {
    Department department = departmentService.findOne(departmentId);
    return new ResponseEntity<>(new GlobalResponse<>(department), HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<GlobalResponse<Department>> createOne(
      @RequestBody @Valid DepartmentCreate department) {
    Department newDepartment = departmentService.createOne(department);

    return new ResponseEntity<>(new GlobalResponse<>(newDepartment), HttpStatus.CREATED);
  }

  @DeleteMapping("/{departmentId}")
  public ResponseEntity<Void> deleteOne(@PathVariable UUID departmentId) {
    departmentService.deleteOne(departmentId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

}
