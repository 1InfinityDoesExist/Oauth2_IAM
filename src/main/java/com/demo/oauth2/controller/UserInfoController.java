package com.demo.oauth2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.demo.oauth2.entity.UserInfo;
import com.demo.oauth2.exception.UserInfoNotFoundException;
import com.demo.oauth2.model.request.UserInfoCreateRequest;
import com.demo.oauth2.model.response.UserInfoCreateResponse;
import com.demo.oauth2.service.UserInfoService;
import com.fb.demo.exception.TenantNotFoundException;
import com.fb.demo.service.impl.InvalidInputException;

@RestController
@RequestMapping(path = "/userInfo")
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    @PostMapping(path = "/create")
    public ResponseEntity<?> createUserInfo(
                    @RequestBody(required = true) UserInfoCreateRequest request) throws Exception {
        try {
            UserInfoCreateResponse response = userInfoService.createUserInfo(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                            .body(new ModelMap().addAttribute("msg", "Successfully created")
                                            .addAttribute("userName", response.getUserName()));
        } catch (final InvalidInputException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new ModelMap().addAttribute("msg", ex.getMessage()));
        } catch (final TenantNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new ModelMap().addAttribute("msg", ex.getMessage()).addAttribute(
                                            "debugMessage", "Please create a tenant first."));
        }
    }

    @GetMapping(path = "/get/{id}")
    public ResponseEntity<?> getUserInfo(@PathVariable(value = "id", required = true) Long id)
                    throws Exception {
        try {
            UserInfo userInfoFromDB = userInfoService.getUserInfo(id);
            return ResponseEntity.status(HttpStatus.OK)
                            .body(new ModelMap().addAttribute("response", userInfoFromDB));
        } catch (final UserInfoNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ModelMap()
                            .addAttribute("msg", ex.getMessage())
                            .addAttribute("debugMessage", "Please create the userInfo first."));
        } catch (final InvalidInputException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new ModelMap().addAttribute("msg", ex.getMessage()));
        }
    }
}
