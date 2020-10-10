package com.demo.oauth2.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import com.demo.oauth2.entity.OtpDetails;
import com.demo.oauth2.entity.UserInfo;
import com.demo.oauth2.exception.UserInfoNotFoundException;
import com.demo.oauth2.model.UserVerificationDetails;
import com.demo.oauth2.repository.OtpDetailsRepository;
import com.demo.oauth2.repository.UserInfoRepository;
import com.demo.oauth2.service.UserVerificationService;
import com.fb.demo.entity.Tenant;
import com.fb.demo.exception.TenantNotFoundException;
import com.fb.demo.repository.TenantRepository;
import com.fb.demo.service.impl.InvalidInputException;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class UserVerificationServiceImpl implements UserVerificationService {

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private OtpDetailsRepository otpDetailsRepository;

    @Autowired
    private TenantRepository tenantRepository;

    @Override
    public List<String> verifyUserInfo(UserVerificationDetails details) throws Exception {
        log.info(":::::Inside UserVerificationService Class, verifyUserInfo method::::");
        if (details == null || StringUtils.isEmpty(details.getUserName())
                        || StringUtils.isEmpty(details.getParentTenant())
                        || ((StringUtils.isEmpty(details.getEmail())
                                        && (StringUtils.isEmpty(details.getMobile()))))) {
            throw new InvalidInputException("Invalid input");
        }
        List<String> response = new ArrayList<String>();
        Tenant tenant = tenantRepository.getTenantByName(details.getParentTenant());
        if (tenant == null) {
            throw new TenantNotFoundException("Tenant not found. Please create a tenant first");
        }
        log.info("::::Tenant Id : {}", tenant.getId());
        UserInfo userInfo = userInfoRepository.findByUsernameAndParentTenant(details.getUserName(),
                        tenant.getId());
        if (userInfo == null) {
            throw new UserInfoNotFoundException("User does not exist");
        }
        log.info("::::UserInfo Id : {}", userInfo.getId());
        OtpDetails otpDetails = otpDetailsRepository.findOtpDetailsById(userInfo.getOtpDetails());
        if (details.getEmailOtp() != null) {
            if (otpDetails.isEmailOtpExpired()) {
                response.add("Email otp has been expired. Please request for a new otp");
            } else {
                if (otpDetails.getEmailOtp().equals(details.getEmailOtp())) {
                    userInfo.setEmailVerified(true);
                    response.add("Successfully verified.");
                    if (userInfo.isMobileVerified()) {
                        otpDetailsRepository.delete(otpDetails);
                    }
                } else {
                    response.add("Incorred otp.");
                }
            }
        }
        userInfoRepository.save(userInfo);
        return response;
    }

}
