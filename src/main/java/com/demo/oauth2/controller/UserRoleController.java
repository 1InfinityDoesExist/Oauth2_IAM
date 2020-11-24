package com.demo.oauth2.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.oauth2.entity.UserRole;
import com.demo.oauth2.exception.UserInfoNotFoundException;
import com.demo.oauth2.exception.UserRoleNotFound;
import com.demo.oauth2.model.request.UserRoleCreateRequest;
import com.demo.oauth2.model.request.UserRoleUpdateRequest;
import com.demo.oauth2.model.response.UserRoleCreateResponse;
import com.demo.oauth2.service.UserRoleService;
import com.fb.demo.exception.TenantNotFoundException;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;

@RestController
@RequestMapping(path = "/userRole")
public class UserRoleController {

	@Autowired
	private UserRoleService userRoleService;

	@PostMapping(path = "/create", consumes = "application/json", produces = "application/json")
	public ResponseEntity<?> createUserRole(@RequestBody(required = true) UserRoleCreateRequest request)
			throws Exception {
		try {
			UserRoleCreateResponse response = userRoleService.createUserRole(request);
			return ResponseEntity.status(HttpStatus.CREATED).body(new ModelMap().addAttribute("msg", response.getMsg())
					.addAttribute("roleName", response.getRoleName()));
		} catch (final TenantNotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ModelMap().addAttribute("msg", ex.getMessage())
					.addAttribute("debugMsg", "Please create a tenant first"));
		}
	}

	@GetMapping(path = "/get/{userRole}")
	public ResponseEntity<?> getUserRoleByName(@PathVariable(value = "userRole", required = true) String userRole)
			throws Exception {
		try {
			UserRole userRoleFromDB = userRoleService.getUserRoleByName(userRole);
			return ResponseEntity.status(HttpStatus.OK).body(new ModelMap().addAttribute("response", userRoleFromDB));
		} catch (final UserRoleNotFound ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ModelMap().addAttribute("msg", ex.getMessage())
					.addAttribute("debugMessage", "Please create a userRole first"));
		}
	}

	@GetMapping(path = "/getAll")
	public ResponseEntity<ModelMap> getAllUserRole() {
		List<UserRole> listOfUserRole = userRoleService.getAllActiveUserRoles();
		return ResponseEntity.status(HttpStatus.OK).body(new ModelMap().addAttribute("response", listOfUserRole));
	}

	@ApiImplicitParams({ @ApiImplicitParam(name = "id", paramType = "header") })
	@DeleteMapping(value = "/delete/{id}")
	public ResponseEntity<ModelMap> deleteUserRole(@PathVariable(value = "id", required = true) Long id)
			throws Exception {
		try {
			userRoleService.deleteUserRoleById(id);
			return ResponseEntity.status(HttpStatus.OK)
					.body(new ModelMap().addAttribute("msg", "Successfully deleted"));
		} catch (final UserRoleNotFound ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ModelMap().addAttribute("errorMsg", ex.getMessage()));
		}
	}

	@PutMapping(value = "/update/{id}")
	public ResponseEntity<ModelMap> updateUserRoleById(@RequestBody UserRoleUpdateRequest request,
			@PathVariable(value = "id", required = true) Long id) throws Exception {
		try {
			userRoleService.updateUserRoleById(request, id);
			return ResponseEntity.status(HttpStatus.OK)
					.body(new ModelMap().addAttribute("msg", "Successfully updated"));
		} catch (final UserInfoNotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ModelMap().addAttribute("errorMsg", ex.getMessage()));
		}
	}
}
