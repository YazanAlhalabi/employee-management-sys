package com.myFullstackYazan.employee_management.services;

import com.myFullstackYazan.employee_management.config.JwtHelper;
import com.myFullstackYazan.employee_management.dtos.LoginRequest;
import com.myFullstackYazan.employee_management.dtos.ResetPasswordRequest;
import com.myFullstackYazan.employee_management.dtos.SignupRequest;
import com.myFullstackYazan.employee_management.entities.Employee;
import com.myFullstackYazan.employee_management.entities.PasswordResetToken;
import com.myFullstackYazan.employee_management.entities.UserAccount;
import com.myFullstackYazan.employee_management.repositpories.EmployeeRepo;
import com.myFullstackYazan.employee_management.repositpories.PasswordRestRepo;
import com.myFullstackYazan.employee_management.repositpories.UserAccountRepo;
import com.myFullstackYazan.employee_management.shared.CustomResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class AuthService {
  @Autowired
  private UserAccountRepo userAccountRepo;
  @Autowired
  private EmployeeRepo employeeRepo;
  @Autowired
  private PasswordRestRepo passwordRestRepo;
  @Autowired
  private EmailService emailService;

  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  private AuthenticationManager authenticationManager;
  @Autowired
  private JwtHelper jwtHelper;

  public void signup(SignupRequest signupRequest, String token) {
    Employee employee = employeeRepo.findOneByAccountCreationToken(token)
        .orElseThrow(() -> CustomResponseException.ResourceNotFound("Invalid token"));

    if (employee.isVerified()) {
      throw CustomResponseException.BadRequest("Account already created");
    }

    UserAccount account = new UserAccount();
    account.setUsername(signupRequest.username());
    account.setPassword(passwordEncoder.encode(signupRequest.password()));
    account.setEmployee(employee);
    userAccountRepo.save(account);

    employee.setVerified(true);
    employee.setAccountCreationToken(null);
    employeeRepo.save(employee);
  }


  public String login(LoginRequest loginRequest) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            loginRequest.username(),
            loginRequest.password()
        )
    );

    UserAccount user = userAccountRepo.findOneByUsername(loginRequest.username())
        .orElseThrow(() -> CustomResponseException.BadCredentials());

    Map<String, Object> customClaims = new HashMap<>();
    customClaims.put("userId", user.getId());
    return jwtHelper.generateToken(customClaims, user);
  }

  @Transactional
  public void initiatePasswordRest(String username) {
    try {
      UserAccount user = userAccountRepo.findOneByUsername(username)
          .orElseThrow(() -> CustomResponseException.ResourceNotFound("Account not found"));

      String token = UUID.randomUUID().toString();
      LocalDateTime expiry = LocalDateTime.now().plusMinutes(15);

      PasswordResetToken resetToken = new PasswordResetToken();
      resetToken.setToken(token);
      resetToken.setExpiryDate(expiry);
      resetToken.setUser(user);

      passwordRestRepo.save(resetToken);

      emailService.sendPasswordRestEmail(user.getEmployee().getEmail(), token);
    } catch (Exception e) {
      throw CustomResponseException.BadRequest("Sending reset password email failed");
    }
  }


  public void resetPassword(ResetPasswordRequest resetPasswordRequest) {
    PasswordResetToken resetToken = passwordRestRepo.findOneByToken(resetPasswordRequest.token())
        .orElseThrow(() -> CustomResponseException.BadRequest("Invalid token"));

    boolean isTokenExpired = resetToken.getExpiryDate().isBefore(LocalDateTime.now());
    if (isTokenExpired) {
      passwordRestRepo.delete(resetToken);
      throw CustomResponseException.BadRequest("Token has expired, request a new one");
    }

    UserAccount user = resetToken.getUser();
    user.setPassword(passwordEncoder.encode(resetPasswordRequest.newPassword()));
    userAccountRepo.save(user);
    passwordRestRepo.delete(resetToken);
  }
}
