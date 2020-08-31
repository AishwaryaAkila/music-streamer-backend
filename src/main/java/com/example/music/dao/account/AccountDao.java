package com.example.music.dao.account;

import java.util.Optional;

import com.example.music.dao.IOperations;
import com.example.music.model.Account;

public interface AccountDao extends IOperations<Account> {

	Optional<Account> findByUsernameOrEmail(String username, String email);

	Optional<Account> findByEmail(String email);

	Optional<Account> findByUsername(String username);
	
}