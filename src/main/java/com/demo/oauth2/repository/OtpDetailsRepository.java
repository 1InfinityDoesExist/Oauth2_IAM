package com.demo.oauth2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.demo.oauth2.entity.OtpDetails;

@Repository
public interface OtpDetailsRepository extends JpaRepository<OtpDetails, Long> {

    public OtpDetails findOtpDetailsById(Long otpDetails);

}
