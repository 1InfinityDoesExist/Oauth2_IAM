package com.demo.oauth2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.demo.oauth2.entity.UserRole;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

	public UserRole findByName(String roleName);

	public List<UserRole> findUserRoleByIsActive(boolean b);

	public UserRole findUserRoleByIdAndIsActive(Long id, boolean b);

}
