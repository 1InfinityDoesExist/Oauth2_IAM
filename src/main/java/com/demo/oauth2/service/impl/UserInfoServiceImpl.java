package com.demo.oauth2.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import com.demo.oauth2.email.EmailService;
import com.demo.oauth2.entity.OtpDetails;
import com.demo.oauth2.entity.UserInfo;
import com.demo.oauth2.entity.UserRole;
import com.demo.oauth2.exception.UserAlreadyExistException;
import com.demo.oauth2.exception.UserInfoNotFoundException;
import com.demo.oauth2.exception.UserRoleNotFound;
import com.demo.oauth2.model.request.UserInfoCreateRequest;
import com.demo.oauth2.model.response.UserInfoCreateResponse;
import com.demo.oauth2.repository.OtpDetailsRepository;
import com.demo.oauth2.repository.UserInfoRepository;
import com.demo.oauth2.repository.UserRoleRepository;
import com.demo.oauth2.service.UserInfoService;
import com.demo.oauth2.util.CommonUtility;
import com.fb.demo.entity.Tenant;
import com.fb.demo.exception.TenantNotFoundException;
import com.fb.demo.repository.TenantRepository;
import com.fb.demo.service.impl.InvalidInputException;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private CommonUtility commonUtility;

    @Autowired
    private OtpDetailsRepository otpDetailsRepository;

    @Value("${otp.expiry.time}")
    public int otpExpiryTime;

    @Override
    public UserInfoCreateResponse createUserInfo(UserInfoCreateRequest request) throws Exception {
        log.info(":::::::UserInfoServiceImpl Class, createUserInfo method:::::");
        UserInfo userInfo;
        if (ObjectUtils.isEmpty(request)) {
            throw new InvalidInputException("request must not be null or empty");
        }
        Tenant tenantFromDB = tenantRepository.getTenantByName(request.getParentTenant());
        if (tenantFromDB == null) {
            throw new TenantNotFoundException("Tenant not found");
        }
        validateUserInfo(request);
        log.info("::::::Tenant Name {}", tenantFromDB.getName());
        userInfo = new UserInfo();
        userInfo.setParentTenant(tenantFromDB.getId());
        OtpDetails otpDetails = getOtpDetailsId();
        if (!StringUtils.isEmpty(request.getEmail())) {
            String response = emailService.sendMail(
                            new ModelMap().addAttribute("subject", "OTP for emailVerification")
                                            .addAttribute("mailBody",
                                                            "Your otp is " + otpDetails
                                                                            .getEmailOtp())
                                            .addAttribute("to", "patelavinashbirgunj@gmail.com"));
            if (response.equalsIgnoreCase("Mail Sent Success")) {
                log.info(":::::mail has been sent successfully::::");
                userInfo.setEmail(request.getEmail());
            } else {
                log.info("::::Inside else part of send email");
                // thow the exception here
            }
        }
        userInfo.setOtpDetails(otpDetails.getId());
        userInfo.setFirstName(request.getFirstName());
        userInfo.setImageUrl(request.getImageUrl());
        userInfo.setLastName(request.getLastName());
        userInfo.setMobile(request.getMobile());
        userInfo.setPassword(encoder.encode(request.getPassword()));
        userInfo.setUsername(request.getUsername());
        userInfo.setRoles(setUserRole(request, userInfo));
        userInfo.setAuthorities(Arrays.asList(new String[] {"Read", "Write", "Execute"}));
        userInfoRepository.save(userInfo);
        UserInfoCreateResponse response = new UserInfoCreateResponse();
        response.setId(userInfo.getId());
        response.setMobile(userInfo.getMobile());
        response.setEmail(userInfo.getEmail());
        response.setUserName(userInfo.getUsername());
        return response;
    }

    private void validateUserInfo(UserInfoCreateRequest request) throws Exception {
        if (StringUtils.isEmpty(request.getEmail()) && StringUtils.isEmpty(request.getMobile())) {
            throw new InvalidInputException("Either Email or Mobile Number Must Be Present");
        }

        Tenant tenantFromDB = tenantRepository.getTenantByName(request.getParentTenant());
        if (tenantFromDB == null) {
            throw new TenantNotFoundException("Tenant not found");
        }
        UserInfo userInfo = null;
        if (!StringUtils.isEmpty(request.getEmail())) {
            userInfo = userInfoRepository
                            .findByEmailAndIsEmailVerifiedAndParentTenant(request.getEmail(), true,
                                            tenantFromDB.getId());
            if (userInfo != null) {
                throw new UserAlreadyExistException(
                                "User already exist with email :" + request.getEmail());
            }
        }
        if (!StringUtils.isEmpty(request.getMobile())) {
            userInfo = userInfoRepository.findByMobileAndIsMobileVerifiedAndParentTenant(
                            request.getMobile(),
                            true, tenantFromDB.getId());
            if (userInfo != null) {
                throw new UserAlreadyExistException(
                                "User already exist with moible :" + request.getMobile());
            }
        }
        if (!StringUtils.isEmpty(request.getUsername())) {
            userInfo = userInfoRepository.findByUsername(request.getUsername());
            if (userInfo != null) {
                throw new UserAlreadyExistException(
                                "User already exist with username as : " + request.getUsername());
            }
        }
    }

    private OtpDetails getOtpDetailsId() {
        OtpDetails otpDetails = new OtpDetails();
        otpDetails.setEmailOtp(commonUtility.generateOTP());
        otpDetails.setMobileOtp(commonUtility.generateOTP());
        otpDetails.setEmailOtpExpiryDate(otpExpiryTime);
        otpDetails.setMobileOtpExpiryDate(otpExpiryTime);
        return otpDetailsRepository.save(otpDetails);
    }

    private List<Long> setUserRole(UserInfoCreateRequest request, UserInfo userInfo)
                    throws Exception {
        if (request.getRoles().isEmpty() || request.getRoles() == null) {
            throw new InvalidInputException("Roles not found in the paylaod");
        }
        List<Long> listOfRoles = new ArrayList<Long>();
        for (String roleName : request.getRoles()) {
            UserRole userRole = userRoleRepository.findByName(roleName);
            if (!ObjectUtils.isEmpty(userRole)) {
                listOfRoles.add(userRole.getId());
            } else {
                throw new UserRoleNotFound("UserRole not found for : " + roleName);
            }
        }
        return listOfRoles;
    }

    @Override
    public UserInfo getUserInfo(Long userInfoId) throws Exception {
        log.info(":::::UserInfoServiceImpl Class, userInfo method::::");
        if (ObjectUtils.isEmpty(userInfoId)) {
            throw new InvalidInputException("userInfoId must not be null or empty");
        }
        Optional<UserInfo> userInfoFromDB = userInfoRepository.findById(userInfoId);
        log.info(":::::UserInfoFromDB {}", userInfoFromDB);
        if (!userInfoFromDB.isPresent()) {
            throw new UserInfoNotFoundException(
                            "UserInfo not found for the given id : " + userInfoId);
        }
        return userInfoFromDB.get();
    }
}
