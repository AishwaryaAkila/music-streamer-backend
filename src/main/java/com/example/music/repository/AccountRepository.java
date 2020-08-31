package com.example.music.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.music.model.Account;

import java.lang.String;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

	Optional<Account> findByUsernameOrEmail(String username, String email);

	Optional<Account> findByEmail(String email);

	Optional<Account> findByUsername(String username);
//	@Query(value = "select password from users where email = ?1", nativeQuery = true)
//	  String findByaccount_id(long acc_id);
	
	
}