package edu.uclm.esi.ds.Games.entities;

import java.util.UUID;

import javax.validation.constraints.NotEmpty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table(name = "users", indexes = { @Index(columnList = "name", unique = true),
		@Index(columnList = "email", unique = true), })
public class User {
	@Id
	@Column(length = 36)
	private String id;
	@Column(length = 100)
	@NotEmpty
	private String name;
	@Column(length = 140)
	@NotEmpty
	private String email;
	@NotEmpty
	private String pwd;
	@NotEmpty
	private Long validationDate;
	@Column(nullable = false, columnDefinition = "TINYINT", length = 1)
	private boolean vip;
	@Column
	@NotEmpty
	private String role;
	public User() {
		this.id = UUID.randomUUID().toString();

	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getPwd() {
		return pwd;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public Long getValidationDate() {
		return validationDate;
	}

	public void setValidationDate(Long validationDate) {
		this.validationDate = validationDate;
	}
	
	public boolean isVip() {
		return vip;
	}

	public void setVip(boolean vip) {
		this.vip = vip;
	}
}