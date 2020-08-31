package com.example.music.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.music.model.VerifyAccount;

public interface VerifyAccountRepository extends JpaRepository<VerifyAccount, Long>{

	Optional<VerifyAccount> findByToken(String token);
	
	
	
	@Query(value="select * from verify_account where account_id=?1 order by id desc limit 1",nativeQuery = true)
	VerifyAccount findToken(Long id);
	
}