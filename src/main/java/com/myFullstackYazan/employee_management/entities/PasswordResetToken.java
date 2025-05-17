package com.myFullstackYazan.employee_management.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "password_rest_token")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetToken {
  @Id
  @GeneratedValue(generator = "UUID")
  @UuidGenerator
  private UUID id;

  @Column(name = "token", unique = true, nullable = false)
  private String token;

  @OneToOne
  @JoinColumn(name = "user_id", nullable = false, unique = true)
  private UserAccount user;

  @Column(nullable = false)
  private LocalDateTime expiryDate;
}
