package com.myFullstackYazan.employee_management.repositpories;

import com.myFullstackYazan.employee_management.entities.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PasswordRestRepo extends JpaRepository<PasswordResetToken, UUID> {
  Optional<PasswordResetToken> findOneByToken(String token);
}
