package com.myFullstackYazan.employee_management.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.UUID;

@Entity
@Table(name = "user-account")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserAccount implements UserDetails {
  @Id
  @GeneratedValue(generator = "UUID")
  @UuidGenerator
  private UUID id;

  @Column(name = "username", unique = true, nullable = false, length = 100)
  private String username;

  @Column(name = "password", nullable = false, length = 100)
  private String password;

  @Column(name = "role", nullable = false, length = 20)
  private String role = "USER";

  @OneToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "employee_id", unique = true, nullable = false)
  private Employee employee;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return null;
  }
}
