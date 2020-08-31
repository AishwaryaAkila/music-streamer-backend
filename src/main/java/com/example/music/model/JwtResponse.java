package com.example.music.model;

import java.io.Serializable;

public class JwtResponse implements Serializable {

	private static final long serialVersionUID = -8091879091924046844L;
	private final String jwttoken;
	private final Long role;

	public JwtResponse(String jwttoken,Long role) {
		this.jwttoken = jwttoken;
		this.role=role;
	}

	public String getToken() {
		return this.jwttoken;
	}
	public Long getRole() {
		return this.role;
	}
}
