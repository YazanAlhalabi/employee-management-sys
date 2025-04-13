package com.myFullstackYazan.employee_management.repositpories;

import com.myFullstackYazan.employee_management.entities.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LeaveRequestRepo extends JpaRepository<LeaveRequest, UUID> {
  List<LeaveRequest> findAllByEmployeeId(UUID employeeUd);
}
