package com.myFullstackYazan.employee_management.controllers;


import com.myFullstackYazan.employee_management.dtos.LoginRequest;
import com.myFullstackYazan.employee_management.dtos.ResetPasswordRequest;
import com.myFullstackYazan.employee_management.dtos.SignupRequest;
import com.myFullstackYazan.employee_management.services.AuthService;
import com.myFullstackYazan.employee_management.shared.GlobalResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
  @Autowired
  private AuthService authService;


  @PostMapping("/login")
  public ResponseEntity<GlobalResponse<String>> login(
      @RequestBody LoginRequest loginRequest
  ) {
    String token = authService.login(loginRequest);

    return new ResponseEntity<>(
        new GlobalResponse<>(token), HttpStatus.CREATED);
  }

  @PostMapping("/signup")
  public ResponseEntity<GlobalResponse<String>> signup(
      @RequestBody SignupRequest signupRequest,
      @RequestParam String token
  ) {

    authService.signup(signupRequest, token);
    return new ResponseEntity<>(new GlobalResponse<>("Signed Up"), HttpStatus.CREATED);
  }

  @PostMapping("/forgot-password/{username}")
  public ResponseEntity<GlobalResponse<String>> forgotPassword(@PathVariable String username) {

    authService.initiatePasswordRest(username);
    return new ResponseEntity<>(new GlobalResponse<>("Password reset email sent!"), HttpStatus.CREATED);
  }

  @PostMapping("/reset-password")
  public ResponseEntity<GlobalResponse<String>> resetPassword(
      @RequestBody ResetPasswordRequest resetPasswordRequest
  ) {
    authService.resetPassword(resetPasswordRequest);
    return new ResponseEntity<>(new GlobalResponse<>("Password reset successfully!"), HttpStatus.CREATED);
  }
}
