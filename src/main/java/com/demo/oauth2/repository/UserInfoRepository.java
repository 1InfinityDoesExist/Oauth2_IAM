package com.demo.oauth2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.demo.oauth2.entity.UserInfo;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {

    public UserInfo findUserInfoById(Long userInfoId);

    public UserInfo findByEmail(String userName);

}
