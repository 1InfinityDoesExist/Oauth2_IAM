package com.demo.oauth2.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.demo.oauth2.entity.UserInfo;
import com.demo.oauth2.entity.UserRole;
import com.demo.oauth2.exception.ApplicationException;
import com.demo.oauth2.exception.Error;
import com.demo.oauth2.repository.UserInfoRepository;
import com.demo.oauth2.repository.UserRoleRepository;
import com.fb.demo.entity.Tenant;
import com.fb.demo.repository.TenantRepository;
import lombok.extern.slf4j.Slf4j;

@Service(value = "oauth2_service_impl")
@Slf4j
@Transactional // "failed to lazily initialize a collection of role:
               // com.demo.oauth2.entity.UserInfo.roles, could not initialize proxy - no Session"
public class Oauth2ServiceImpl implements UserDetailsService {

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private TenantRepository tenantRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("::::::Insdie Oauth2ServiceImpl Class, loadUserByUserName method:::::");
        HttpServletRequest request =
                        ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                                        .getRequest();
        String parentTenant = request.getParameter("tenantId");
        String token = request.getParameter("token");
        String role = request.getParameter("role");
        String grantType = request.getParameter("grant_type");
        String provider = request.getParameter("provider");
        String password = request.getParameter("password");
        UserDetails userInfoDetails =
                        getUserInfoDetails(username, parentTenant, token, role, grantType, provider,
                                        password);
        log.info("::::::UserDetails {}", userInfoDetails);
        return userInfoDetails;
    }

    private UserDetails getUserInfoDetails(String userName, String parentTenant, String token,
                    String role,
                    String grantType, String provider, String password) {
        log.info("::::::Oauth2ServiceImpl Class, getUserInfoDetails method :::::");
        UserInfo userInfo = getUserInfo(userName, parentTenant);
        log.info(":::::userInfo {}", userInfo);
        UserDetails userDetails = new User(userName, new BCryptPasswordEncoder().encode(password),
                        getAuthorityForUserInfo(userInfo));
        return userDetails;
    }

    private UserInfo getUserInfo(String userName, String parentTenant) {
        Tenant tenant = null;
        if (!StringUtils.isEmpty(parentTenant)) {
            tenant = tenantRepository.getTenantByName(parentTenant);
            if (tenant == null) {
                throw new ApplicationException(HttpStatus.NOT_FOUND, Error.NOT_FOUND,
                                "Tenant not Found");
            }
        }
        String mobileRegex = "[0-9]+";
        String emailRegex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        UserInfo userInfo = null;
        if (userName.matches(emailRegex)) {
            userInfo = userInfoRepository.findByEmailAndIsEmailVerifiedAndParentTenant(userName,
                            true,
                            tenant.getId());
        }
        if (userName.matches(mobileRegex)) {
            userInfo = userInfoRepository.findByMobileAndIsMobileVerifiedAndParentTenant(userName,
                            true,
                            tenant.getId());
        }
        if (userInfo == null) {
            userInfo = userInfoRepository.findByUsernameAndParentTenant(userName, tenant.getId());
        }
        if (userInfo == null) {
            log.info("::::::UserInfo is null");
            throw new ApplicationException(HttpStatus.NOT_FOUND, Error.NOT_FOUND, "User not Found");
        }
        return userInfo;
    }

    private Collection<? extends GrantedAuthority> getAuthorityForUserInfo(UserInfo userInfo) {
        log.info("::::::Oauth2ServiceImpl Class, getAuthorityForUserInfo method::::::");
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        log.info(":::::::::here");
        for (Long userRole : userInfo.getRoles()) {
            log.info("::::::userRole : {}", userRole);
            Optional<UserRole> optional = userRoleRepository.findById(userRole);
            if (optional.isPresent()) {
                authorityList.add(new SimpleGrantedAuthority("ROLE_" + optional.get().getName()));
            }
        }
        log.info(":::::AuthorityList {}", authorityList);
        return authorityList;
    }
}
