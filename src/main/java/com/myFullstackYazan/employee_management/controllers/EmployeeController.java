package com.myFullstackYazan.employee_management.controllers;

import com.myFullstackYazan.employee_management.abstracts.EmployeeService;
import com.myFullstackYazan.employee_management.abstracts.LeaveRequestService;
import com.myFullstackYazan.employee_management.dtos.EmployeeCreate;
import com.myFullstackYazan.employee_management.dtos.EmployeeUpdate;
import com.myFullstackYazan.employee_management.dtos.LeaveRequestCreate;
import com.myFullstackYazan.employee_management.dtos.PaginatedResponse;
import com.myFullstackYazan.employee_management.entities.Employee;
import com.myFullstackYazan.employee_management.entities.LeaveRequest;
import com.myFullstackYazan.employee_management.shared.GlobalResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
  @Autowired
  private EmployeeService employeeService;

  @Autowired
  private LeaveRequestService leaveRequestService;

  @GetMapping
  public ResponseEntity<GlobalResponse<PaginatedResponse<Employee>>> findAll(
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "3") int size,
      HttpServletRequest request
  ) {
    int zeroBasedPage = page - 1;
    Page<Employee> employees = employeeService.findAll(zeroBasedPage, size);

    String baseUrl = request.getRequestURL().toString();
    String nextUrl = employees.hasNext() ? String.format("%s?page=%d&size=%d", baseUrl, page + 1, size) : null;
    String prevUrl = employees.hasPrevious() ? String.format("%s?page=%d&size=%d", baseUrl, page - 1, size) : null;

    var paginatedResponse = new PaginatedResponse<Employee>(
        employees.getContent(),
        employees.getNumber(),
        employees.getTotalPages(),
        employees.getTotalElements(),
        employees.hasNext(),
        employees.hasPrevious(),
        nextUrl,
        prevUrl
    );
    return new ResponseEntity<>(new GlobalResponse<>(paginatedResponse), HttpStatus.OK);
  }

  @GetMapping("/{employeeId}")
  public ResponseEntity<GlobalResponse<Employee>> findOne(@PathVariable UUID employeeId) {
    Employee employee = employeeService.findOne(employeeId);

    return new ResponseEntity<>(new GlobalResponse<>(employee), HttpStatus.OK);
  }

  @DeleteMapping("/{employeeId}")
  public ResponseEntity<Void> deleteOne(@PathVariable UUID employeeId) {
    employeeService.deleteOne(employeeId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @PutMapping("{employeeId}")
  public ResponseEntity<GlobalResponse<Employee>> updateOne(
      @PathVariable UUID employeeId,
      @RequestBody @Valid EmployeeUpdate employee) {

    Employee updatedEmployee = employeeService.updateOne(employeeId, employee);

    return new ResponseEntity<>(new GlobalResponse<>(updatedEmployee), HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<GlobalResponse<Employee>> createOne(
      @RequestBody @Valid EmployeeCreate employee) {
    Employee newEmployee = employeeService.createOne(employee);

    return new ResponseEntity<>(new GlobalResponse<>(newEmployee), HttpStatus.CREATED);
  }

  @PostMapping("/{employeeId}/leave-requests")
  public ResponseEntity<GlobalResponse<LeaveRequest>> leaveRequest(
      @RequestBody @Valid LeaveRequestCreate leaveRequest,
      @PathVariable UUID employeeId) {

    LeaveRequest newLeaveRequest = leaveRequestService.createOne(leaveRequest, employeeId);

    return new ResponseEntity<>(new GlobalResponse<>(newLeaveRequest), HttpStatus.CREATED);
  }

  @GetMapping("/{employeeId}/leave-requests")
  public ResponseEntity<GlobalResponse<List<LeaveRequest>>> leaveRequestsByEmployeeId(
      @PathVariable UUID employeeId
  ) {
    List<LeaveRequest> leaveRequests = leaveRequestService.findAllByEmployeeId(employeeId);
    return new ResponseEntity<>(new GlobalResponse<>(leaveRequests), HttpStatus.OK);
  }

}
