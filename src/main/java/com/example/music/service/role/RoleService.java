package com.example.music.service.role;

import java.util.Optional;

import com.example.music.model.Role;

public interface RoleService {

	Optional<Role> findById(Long id);
	Role create(Role role);
}
