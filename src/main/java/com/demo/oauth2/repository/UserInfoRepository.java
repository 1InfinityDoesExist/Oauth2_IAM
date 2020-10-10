package com.demo.oauth2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.demo.oauth2.entity.UserInfo;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {

    public UserInfo findUserInfoById(Long userInfoId);

    public UserInfo findByUsername(String username);

    public UserInfo findByUsernameAndParentTenant(String userName, Integer parentTenant);

    public UserInfo findByEmailAndIsEmailVerifiedAndParentTenant(String userName, boolean b,
                    Integer id);

    public UserInfo findByMobileAndIsMobileVerifiedAndParentTenant(String userName, boolean b,
                    Integer id);

}
