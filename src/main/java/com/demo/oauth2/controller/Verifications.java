package com.demo.oauth2.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.demo.oauth2.exception.UserInfoNotFoundException;
import com.demo.oauth2.model.UserVerificationDetails;
import com.demo.oauth2.service.UserVerificationService;
import com.fb.demo.exception.TenantNotFoundException;
import com.fb.demo.service.impl.InvalidInputException;

@RestController
@RequestMapping(path = "/verify")
public class Verifications {

    @Autowired
    private UserVerificationService userVerificationService;

    @PostMapping(path = "/emailMobile")
    public ResponseEntity<?> verifyUserInfoDetails(
                    @RequestBody UserVerificationDetails userVerificationDetails) throws Exception {
        try {
            List<String> response = userVerificationService.verifyUserInfo(userVerificationDetails);
            return ResponseEntity.status(HttpStatus.OK)
                            .body(new ModelMap().addAttribute("response", response));
        } catch (final InvalidInputException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(new ModelMap().addAttribute("msg", ex.getMessage()));
        } catch (final TenantNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new ModelMap().addAttribute("msg", ex.getMessage()));
        } catch (final UserInfoNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new ModelMap().addAttribute("msg", ex.getMessage()));
        }
    }
}
