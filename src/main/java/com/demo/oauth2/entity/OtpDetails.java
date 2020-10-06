package com.demo.oauth2.entity;

import java.util.Calendar;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "OtpDetails")
@Table(name = "otp_details")
@lombok.Data
public class OtpDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer mobileOtp;
    private Integer emailOtp;

    /** Email Otp Expire Date **/
    private Date emailOtpExpiryDate;

    public Date getEmailOtpExpiryDate() {
        return emailOtpExpiryDate;
    }

    public void setEmailOtpExpiryDate(Date emailOtpExpiryDate) {
        this.emailOtpExpiryDate = emailOtpExpiryDate;
    }

    public void setEmailOtpExpiryDate(int minutes) {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.MINUTE, minutes);
        this.emailOtpExpiryDate = now.getTime();
    }

    public boolean isEmailOtpExpired() {
        return new Date().after(this.emailOtpExpiryDate);
    }

    /* Mobile Otp Expire Date **/
    private Date mobileOtpExpiryDate;

    public Date getMobileOtpExpiryDate() {
        return mobileOtpExpiryDate;
    }

    public void setMobileOtpExpiryDate(Date mobileOtpExpiryDate) {
        this.mobileOtpExpiryDate = mobileOtpExpiryDate;
    }

    public void setMobileOtpExpiryDate(int minutes) {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.MINUTE, minutes);
        this.mobileOtpExpiryDate = now.getTime();
    }

    public boolean isMobileOtpExpired() {
        return new Date().after(this.mobileOtpExpiryDate);
    }
}
