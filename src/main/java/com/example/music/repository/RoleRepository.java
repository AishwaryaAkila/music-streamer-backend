package com.example.music.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import com.example.music.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long>{
	
	
}