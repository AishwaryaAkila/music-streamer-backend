package com.example.music.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.music.dao.account.AccountDao;
import com.example.music.model.Account;

@Service
public class JwtUserDetailsService implements UserDetailsService {
	@Autowired
	private AccountDao accountDao;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//		
		try {
			Account check=accountDao.findByEmail(username).get();
			
			return new User(check.getEmail(),check.getPassword(),
					new ArrayList<>());
			
		}
		catch (Exception e) {
			
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
	}

}
