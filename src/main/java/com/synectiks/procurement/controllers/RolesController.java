package com.synectiks.procurement.controllers;

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

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.synectiks.procurement.business.service.RolesService;
import com.synectiks.procurement.domain.Roles;
import com.synectiks.procurement.web.rest.errors.UniqueConstraintException;

import io.github.jhipster.web.util.HeaderUtil;

@RestController
@RequestMapping("/api")
public class RolesController {
	private static final Logger logger = LoggerFactory.getLogger(RolesController.class);

	@Autowired
	RolesService rolesService;

	@PostMapping("/roles")
	public ResponseEntity<Roles> addRoles(@RequestBody ObjectNode obj) throws UniqueConstraintException {
		logger.info("Request to add a role");
		Roles roles=null;
		try {
			 roles = rolesService.addRoles(obj);
			return ResponseEntity.status(HttpStatus.OK).body(roles);
		}catch (UniqueConstraintException e) {
			logger.error("Add role failed. UniqueConstraintException: ", e);
			throw e;
	}
}

	@PutMapping("/roles")
	public ResponseEntity<Roles> updateRoles(@RequestBody ObjectNode obj) throws UniqueConstraintException {
		logger.info("Request to update a role");
		Roles roles=null;
		try {
			 roles = rolesService.updateRoles(obj);
			return ResponseEntity.status(HttpStatus.OK).body(roles);
		}catch (UniqueConstraintException e) {
			logger.error("Update role failed. Exception: ", e.getMessage());
			throw e;
		} 
	}

	@GetMapping("/roles")
	public ResponseEntity<List<Roles>> searchRoles(@RequestParam Map<String, String> requestObj) {
		logger.info("Request to search role on given filter criteria");
			List<Roles> list = rolesService.searchRoles(requestObj);
			return ResponseEntity.status(HttpStatus.OK).body(list);
	}

	@DeleteMapping("/roles/{id}")
	public ResponseEntity<Void> deleteRoles(@PathVariable Long id) {
		logger.info("Request to delete a role");
			rolesService.deleteRoles(id);
			return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert("roles", false, "roles", id.toString())).build();
			
	}

	@GetMapping("/roles/{id}")
	public ResponseEntity<Roles> getRoles(@PathVariable Long id) {
		logger.info("Getting roles by id: " + id);
			Roles roles = rolesService.getRoles(id);
			return ResponseEntity.status(HttpStatus.OK).body(roles);
	}
}
