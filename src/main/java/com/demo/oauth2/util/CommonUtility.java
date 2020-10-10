package com.demo.oauth2.util;

import java.util.Random;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CommonUtility {

    @Value("${otp.start.range}")
    public int otpStartRange;

    @Value("${otp.end.range}")
    public int otpEndRange;

    public int generateOTP() {
        return otpStartRange + new Random().nextInt(otpEndRange);
    }
}
