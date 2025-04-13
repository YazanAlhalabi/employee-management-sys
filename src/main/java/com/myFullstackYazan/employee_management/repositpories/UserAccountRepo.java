package com.myFullstackYazan.employee_management.repositpories;

import com.myFullstackYazan.employee_management.entities.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserAccountRepo extends JpaRepository<UserAccount, UUID> {
  Optional<UserAccount> findOneByUsername(String username);

  @Query("""
      SELECT COUNT(u) > 0 FROM UserAccount u
      WHERE u.username = :username AND u.employee.id = :employeeId
      """)
  public boolean isOwner(@Param("username") String username, @Param("employeeId") UUID employeeId);
}
