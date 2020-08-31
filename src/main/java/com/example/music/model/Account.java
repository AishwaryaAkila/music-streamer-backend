package com.example.music.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "USERS")
public class Account implements Serializable{

	private static final long serialVersionUID = -2460659701384032012L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "USERNAME", length = 100)
	private String username;
	

	@Column(name = "EMAIL", length = 100, unique = true)
	private String email;

	@Column(name = "PASSWORD")
	private String password;
	
	@Column(name = "is_active")
	private boolean active;
	
	@Column(name= "role")
	private Long role;

//	@JsonIgnore
//	@ManyToMany(fetch = FetchType.LAZY)
//	@JoinTable(name = "user_role", joinColumns = {
//			@JoinColumn(name = "user_id", referencedColumnName = "id") }, inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
//	private Set<Role> roles;
	
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "account", cascade = CascadeType.ALL)
	private Set<VerifyAccount> verifyAccounts;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getRole() {
		return role;
	}

	public void setRole(Long role) {
		this.role = role;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

//	public Set<Role> getRoles() {
//		return roles;
//	}
//
//	public void setRoles(Set<Role> roles) {
//		this.roles = roles;
//	}

	public Set<VerifyAccount> getVerifyAccounts() {
		return verifyAccounts;
	}

	public void setVerifyAccounts(Set<VerifyAccount> verifyAccounts) {
		this.verifyAccounts = verifyAccounts;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

//	public Set<Role> addRole(Role role) {
//		if(roles == null) {
//			roles = new HashSet<Role>();
//		}
//		roles.add(role);
//		return roles;
//	}
}
