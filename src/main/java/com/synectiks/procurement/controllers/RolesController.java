package com.synectiks.procurement.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.synectiks.procurement.business.service.RolesService;
import com.synectiks.procurement.config.BusinessValidationCodes;
import com.synectiks.procurement.config.Constants;
import com.synectiks.procurement.domain.Roles;
import com.synectiks.procurement.web.rest.errors.DataNotFoundException;
import com.synectiks.procurement.web.rest.errors.IdNotFoundException;
import com.synectiks.procurement.web.rest.errors.NegativeIdException;
import com.synectiks.procurement.web.rest.errors.UniqueConstraintException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "Role APIs")
@RestController
@RequestMapping("/api")
public class RolesController {
	private static final Logger logger = LoggerFactory.getLogger(RolesController.class);

	@Autowired
	RolesService rolesService;

	@ApiOperation(value = "Create a new role")
	@PostMapping("/roles")
	public ResponseEntity<Roles> addRoles(@RequestBody ObjectNode obj) throws UniqueConstraintException {
		logger.info("Request to add a role");
		Roles roles = null;
		try {
			roles = rolesService.addRoles(obj);
			return ResponseEntity.status(HttpStatus.OK).body(roles);
		} catch (UniqueConstraintException e) {
			logger.error("Add role failed. UniqueConstraintException: ", e);
			throw e;
		} catch (Exception e) {
			logger.error("Add role failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}
	}

	@ApiOperation(value = "Update an existing role")
	@PutMapping("/roles")
	public ResponseEntity<Roles> updateRoles(@RequestBody ObjectNode obj) throws UniqueConstraintException {
		logger.info("Request to update a role");
		Roles roles = null;
		try {
			roles = rolesService.updateRoles(obj);
			return ResponseEntity.status(HttpStatus.OK).body(roles);
		} catch (UniqueConstraintException e) {
			logger.error("Update role failed. Exception: ", e.getMessage());
			throw e;
		} catch (NegativeIdException e) {
			logger.error("Update role failed. NegativeIdException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.NEGATIVE_ID_NOT_ALLOWED.value()).body(null);
		} catch (IdNotFoundException e) {
			logger.error("Update role failed. IdNotFoundException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.ID_NOT_FOUND.value()).body(null);
		} catch (DataNotFoundException e) {
			logger.error("Update role failed. DataNotFoundException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.DATA_NOT_FOUND.value()).body(null);
		} catch (Exception e) {
			logger.error("Update role failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}
	}

	@ApiOperation(value = "Search role")
	@GetMapping("/roles")
	public ResponseEntity<List<Roles>> searchRoles(@RequestParam Map<String, String> requestObj) {
		logger.info("Request to search role on given filter criteria");
		try {
			List<Roles> list = rolesService.searchRoles(requestObj);
			return ResponseEntity.status(HttpStatus.OK).body(list);
		} catch (NegativeIdException e) {
			logger.error("Search roles failed. NegativeIdException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.NEGATIVE_ID_NOT_ALLOWED.value()).body(null);
		} catch (Exception e) {
			logger.error("Search roles failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}
	}

	@ApiOperation(value = "Delete a role")
	@DeleteMapping("/roles/{id}")
	public ResponseEntity<Boolean> deleteroles(@PathVariable Long id) {
		logger.info("Request to delete a roles");
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode obj = mapper.createObjectNode();
		obj.put("id", id);
		obj.put("status", Constants.STATUS_DEACTIVE);
		try {
			Roles bu = rolesService.updateRoles(obj);
			if (Constants.STATUS_DEACTIVE.equalsIgnoreCase(bu.getStatus())) {
				return ResponseEntity.status(HttpStatus.OK).body(Boolean.TRUE);
			} else {
				return ResponseEntity.status(BusinessValidationCodes.DELETION_FAILED.value()).body(Boolean.FALSE);
			}
		} catch (NegativeIdException e) {
			logger.error("Delete roles failed. NegativeIdException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.NEGATIVE_ID_NOT_ALLOWED.value()).body(null);
		} catch (IdNotFoundException e) {
			logger.error("Delete roles failed. IdNotFoundException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.ID_NOT_FOUND.value()).body(null);
		} catch (DataNotFoundException e) {
			logger.error("Delete roles failed. DataNotFoundException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.DATA_NOT_FOUND.value()).body(null);
		} catch (Exception e) {
			logger.error("Delete roles failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}

	}

//	@ApiOperation(value = "Delete a role")
//	@DeleteMapping("/roles/{id}")
//	public ResponseEntity<Void> deleteRoles(@PathVariable Long id) {
//		logger.info("Request to delete a role");
//		rolesService.deleteRoles(id);
//		return ResponseEntity.noContent()
//				.headers(HeaderUtil.createEntityDeletionAlert("roles", false, "roles", id.toString())).build();
//
//	}

	@ApiOperation(value = "Search a role by id")
	@GetMapping("/roles/{id}")
	public ResponseEntity<Roles> getRoles(@PathVariable Long id) {
		logger.info("Getting roles by id: " + id);

		Map<String, String> reqObj = new HashMap<>();
		try {
			Roles roles = null;
			reqObj.put("id", String.valueOf(id));
			List<Roles> roleList = rolesService.searchRoles(reqObj);
			if (roleList.size() > 0) {
				roles = roleList.get(0);
			}
			return ResponseEntity.status(HttpStatus.OK).body(roles);
		} catch (NegativeIdException e) {
			logger.error("Getting role by id failed. NegativeIdException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.NEGATIVE_ID_NOT_ALLOWED.value()).body(null);
		} catch (Exception e) {
			logger.error("Getting role by id failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}
	}
}
