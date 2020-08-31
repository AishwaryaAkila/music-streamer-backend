package com.example.music.service.role;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.music.dao.role.RoleDao;
import com.example.music.model.Role;

@Service
public class RoleServiceImpl implements RoleService{

	@Autowired
	private RoleDao roleDao;
	
	@Override
	public Optional<Role> findById(Long id) {
		return roleDao.findById(id);
	}

	@Override
	public Role create(Role role) {
		return roleDao.create(role);
	}

}
