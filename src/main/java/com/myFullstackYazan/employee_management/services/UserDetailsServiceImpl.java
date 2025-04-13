package com.myFullstackYazan.employee_management.services;

import com.myFullstackYazan.employee_management.entities.UserAccount;
import com.myFullstackYazan.employee_management.repositpories.UserAccountRepo;
import com.myFullstackYazan.employee_management.shared.CustomResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  @Autowired
  private UserAccountRepo userAccountRepo;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<UserAccount> account = userAccountRepo.findOneByUsername(username);
    if (account.isEmpty()) {
      throw CustomResponseException.BadCredentials();
    }
    UserAccount user = account.get();


    return User.builder()
        .username(user.getUsername())
        .password(user.getPassword())
        .roles(user.getRole())
        .build();
  }
}
