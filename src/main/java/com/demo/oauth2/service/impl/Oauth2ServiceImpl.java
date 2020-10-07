package com.demo.oauth2.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.demo.oauth2.entity.UserInfo;
import com.demo.oauth2.entity.UserRole;
import com.demo.oauth2.repository.UserRoleRepository;
import lombok.extern.slf4j.Slf4j;

@Service(value = "oauth2_service_impl")
@Slf4j
public class Oauth2ServiceImpl implements UserDetailsService {

    @Autowired
    private UserRoleRepository userRoleRepository;


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
        UserInfo userInfo = new UserInfo();
        UserDetails userDetails = new User(userName, password, getAuthorityForUserInfo(userInfo));
        return userDetails;
    }

    private Collection<? extends GrantedAuthority> getAuthorityForUserInfo(UserInfo userInfo) {
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        for (Long userRole : userInfo.getRoles()) {
            Optional<UserRole> optional = userRoleRepository.findById(userRole);
            if (optional.isPresent()) {
                authorityList.add(new SimpleGrantedAuthority("ROLE_" + optional.get().getName()));
            }
        }
        log.info(":::::AuthorityList {}", authorityList);
        return authorityList;
    }

}
