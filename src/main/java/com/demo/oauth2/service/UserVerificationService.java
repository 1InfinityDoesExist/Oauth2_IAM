package com.demo.oauth2.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.demo.oauth2.model.UserVerificationDetails;

@Service
public interface UserVerificationService {
    public List<String> verifyUserInfo(UserVerificationDetails details) throws Exception;

}
