package com.demo.oauth2.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonInclude;

@Entity(name = "UserRole")
@Table(name = "user_role", uniqueConstraints = { @UniqueConstraint(columnNames = { "name" }) })
@lombok.Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRole implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "name", unique = true, insertable = true, updatable = false, nullable = false)
	private String name;
	private String description;
	private Integer tenant;
	private boolean isActive;
}
