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
import com.demo.oauth2.entity.UserRole;
import com.demo.oauth2.exception.UserRoleNotFound;
import com.demo.oauth2.model.request.UserRoleCreateRequest;
import com.demo.oauth2.model.response.UserRoleCreateResponse;
import com.demo.oauth2.service.UserRoleService;
import com.fb.demo.exception.TenantNotFoundException;

@RestController
@RequestMapping(path = "/userRole")
public class UserRoleController {

    @Autowired
    private UserRoleService userRoleService;

    @PostMapping(path = "/create", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> createUserRole(
                    @RequestBody(required = true) UserRoleCreateRequest request) throws Exception {
        try {
            UserRoleCreateResponse response = userRoleService.createUserRole(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                            .body(new ModelMap().addAttribute("msg", response.getMsg())
                                            .addAttribute("roleName", response.getRoleName()));
        } catch (final TenantNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new ModelMap().addAttribute("msg", ex.getMessage()).addAttribute(
                                            "debugMsg", "Please create a tenant first"));
        }
    }

    @GetMapping(path = "/get/{userRole}")
    public ResponseEntity<?> getUserRoleByName(
                    @PathVariable(value = "userRole", required = true) String userRole)
                    throws Exception {
        try {
            UserRole userRoleFromDB = userRoleService.getUserRoleByName(userRole);
            return ResponseEntity.status(HttpStatus.OK)
                            .body(new ModelMap().addAttribute("response", userRoleFromDB));
        } catch (final UserRoleNotFound ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new ModelMap().addAttribute("msg", ex.getMessage()).addAttribute(
                                            "debugMessage", "Please create a userRole first"));
        }
    }
}
