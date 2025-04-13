package com.myFullstackYazan.employee_management.abstracts;

import com.myFullstackYazan.employee_management.dtos.LeaveRequestCreate;
import com.myFullstackYazan.employee_management.entities.LeaveRequest;

import java.util.List;
import java.util.UUID;

public interface LeaveRequestService {
  LeaveRequest createOne(LeaveRequestCreate leaveRequest, UUID employeeId);

  List<LeaveRequest> findAllByEmployeeId(UUID employeeId);
}
