package com.demo.oauth2.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import com.demo.oauth2.entity.UserRole;
import com.demo.oauth2.exception.UserRoleNotFound;
import com.demo.oauth2.model.request.UserRoleCreateRequest;
import com.demo.oauth2.model.response.UserRoleCreateResponse;
import com.demo.oauth2.repository.UserRoleRepository;
import com.demo.oauth2.service.UserRoleService;
import com.fb.demo.entity.Tenant;
import com.fb.demo.repository.TenantRepository;
import com.fb.demo.service.impl.InvalidInputException;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class UserRoleServiceImpl implements UserRoleService {

    @Autowired(required = true)
    private TenantRepository tenantRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Override
    public UserRoleCreateResponse createUserRole(UserRoleCreateRequest request) throws Exception {
        log.info(":::::UserRoleServiceImpl Class, createUserRole method:::::");
        if (request != null && StringUtils.isEmpty(request.getParentTenant())) {
            throw new InvalidInputException("parentTenant must not be null or empty");
        }
        UserRole userRole = new UserRole();
        Tenant tenantFromDB = tenantRepository.getTenantByName(request.getParentTenant());
        if (tenantFromDB != null) {
            userRole.setTenant(tenantFromDB.getId());
        }
        userRole.setActive(true);
        userRole.setDescription(request.getDescription());
        userRole.setName(request.getName());
        userRoleRepository.save(userRole);
        UserRoleCreateResponse response = new UserRoleCreateResponse();
        response.setId(userRole.getId());
        response.setMsg("Successfully created userRole");
        response.setRoleName(userRole.getName());
        return response;
    }

    @Override
    public UserRole getUserRoleByName(String roleName) throws Exception {
        if (StringUtils.isEmpty(roleName)) {
            throw new InvalidInputException("roleName must not be null or empty");
        }
        UserRole userRole = userRoleRepository.findByName(roleName);
        if (userRole != null) {
            return userRole;
        } else {
            throw new UserRoleNotFound("No userRole exist for the given roleName :" + roleName);
        }
    }
}
