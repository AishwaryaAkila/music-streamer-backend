package com.example.music.dao.verify_account.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.music.dao.verify_account.VerifyAccountDao;
import com.example.music.model.VerifyAccount;
import com.example.music.repository.VerifyAccountRepository;

@Repository
public class VerifyAccountDaoImpl implements VerifyAccountDao{

	@Autowired
	private VerifyAccountRepository verifyAccountRepository;
	
	@Override
	public VerifyAccount create(VerifyAccount verifyAccount) {
		return verifyAccountRepository.save(verifyAccount);
	}

	@Override
	public Optional<VerifyAccount> findByToken(String token) {
		return verifyAccountRepository.findByToken(token);
	}

	@Override
	public Optional<VerifyAccount> findById(Long id) {
		return verifyAccountRepository.findById(id);
	}

	

}
