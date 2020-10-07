package com.demo.oauth2.service;

import org.springframework.stereotype.Service;
import com.demo.oauth2.entity.UserRole;
import com.demo.oauth2.model.request.UserRoleCreateRequest;
import com.demo.oauth2.model.response.UserRoleCreateResponse;

@Service
public interface UserRoleService {

    public UserRoleCreateResponse createUserRole(UserRoleCreateRequest request) throws Exception;

    public UserRole getUserRoleByName(String roleName) throws Exception;
}
