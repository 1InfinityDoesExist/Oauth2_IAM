package com.demo.oauth2.service;

import java.util.List;

import org.springframework.stereotype.Service;
import com.demo.oauth2.entity.UserInfo;
import com.demo.oauth2.model.request.UserInfoCreateRequest;
import com.demo.oauth2.model.request.UserInfoUpdateRequest;
import com.demo.oauth2.model.response.UserInfoCreateResponse;

@Service
public interface UserInfoService {

	public UserInfoCreateResponse createUserInfo(UserInfoCreateRequest request) throws Exception;

	public UserInfo getUserInfo(Long userInfoId) throws Exception;

	public List<UserInfo> getUserInfo();

	public void deleteUserById(Long id) throws Exception;

	public void updateUserInfo(UserInfoUpdateRequest userInfoUpdateReqeust, Long id) throws Exception;

}
