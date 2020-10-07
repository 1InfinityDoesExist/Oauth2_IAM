package com.demo.oauth2.service;

import org.springframework.stereotype.Service;
import com.demo.oauth2.entity.UserInfo;
import com.demo.oauth2.model.request.UserInfoCreateRequest;
import com.demo.oauth2.model.response.UserInfoCreateResponse;

@Service
public interface UserInfoService {

    public UserInfoCreateResponse createUserInfo(UserInfoCreateRequest request) throws Exception;

    public UserInfo getUserInfo(Long userInfoId) throws Exception;

}
