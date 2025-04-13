package com.myFullstackYazan.employee_management.utils;


import com.myFullstackYazan.employee_management.repositpories.UserAccountRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SecurityUtils {

  @Autowired
  private UserAccountRepo userAccountRepo;

  public boolean isOwner(UUID incomingEmployeeId) {
    final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    System.out.println(userDetails.getUsername());
    System.out.println("HERE " + incomingEmployeeId);


    return userAccountRepo.isOwner(userDetails.getUsername(), incomingEmployeeId);
  }
}
