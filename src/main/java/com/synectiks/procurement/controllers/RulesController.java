package com.synectiks.procurement.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
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
import com.synectiks.procurement.business.service.RulesService;
import com.synectiks.procurement.config.BusinessValidationCodes;
import com.synectiks.procurement.domain.Rules;
import com.synectiks.procurement.web.rest.errors.DataNotFoundException;
import com.synectiks.procurement.web.rest.errors.IdNotFoundException;
import com.synectiks.procurement.web.rest.errors.NegativeIdException;
import com.synectiks.procurement.web.rest.errors.UniqueConstraintException;

import io.github.jhipster.web.util.HeaderUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "Rule APIs")
@RestController
@RequestMapping("/api")
public class RulesController {
	private static final Logger logger = LoggerFactory.getLogger(RulesController.class);

	@Autowired
	RulesService rulesService;

	@ApiOperation(value = "Create a new rules")
	@PostMapping("/rules")
	public ResponseEntity<Rules> addRules(@RequestBody ObjectNode obj) throws UniqueConstraintException {
		logger.info("Request to add a rule");
		Rules rules;
		try {
			rules = rulesService.addRules(obj);
			return ResponseEntity.status(HttpStatus.OK).body(rules);
		} catch (UniqueConstraintException e) {
			logger.error("Add rules failed. UniqueConstraintException: ", e);
			throw e;
		} catch (Exception e) {
			logger.error("Add rule failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}
	}
	@ApiOperation(value = "Update an existing rules")
	@PutMapping("/rules")
	public ResponseEntity<Rules> updateRules(@RequestBody ObjectNode obj)
			throws UniqueConstraintException, JSONException {
		logger.info("Request to update a rule");
		Rules rules;
		try {
			rules = rulesService.updateRules(obj);
			return ResponseEntity.status(HttpStatus.OK).body(rules);
		} catch (UniqueConstraintException e) {
			logger.error("Update rules failed. UniqueConstraintException: ", e);
			throw e;
		} catch (JSONException e) {
			logger.error("Update rules failed. JSONException: ", e);
			return ResponseEntity.status(BusinessValidationCodes.JSON_EXCEPTION.value()).body(null);
		} catch (NegativeIdException e) {
			logger.error("Update rules failed. NegativeIdException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.NEGATIVE_ID_NOT_ALLOWED.value()).body(null);
		} catch (IdNotFoundException e) {
			logger.error("Update rules failed. IdNotFoundException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.ID_NOT_FOUND.value()).body(null);
		} catch (DataNotFoundException e) {
			logger.error("Update rules failed. DataNotFoundException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.DATA_NOT_FOUND.value()).body(null);
		} catch (Exception e) {
			logger.error("Update rules failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}
	}

	@ApiOperation(value = "Search rules")
	@GetMapping("/rules")
	public ResponseEntity<List<Rules>> searchRules(@RequestParam Map<String, String> requestObj) {
		logger.info("Request to search rule on given filter criteria");

		try {
			List<Rules> list = rulesService.searchRules(requestObj);
			return ResponseEntity.status(HttpStatus.OK).body(list);
		} catch (NegativeIdException e) {
			logger.error("Search rules failed. NegativeIdException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.NEGATIVE_ID_NOT_ALLOWED.value()).body(null);
		} catch (Exception e) {
			logger.error("Search rules failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}
	}
	
	
	@ApiOperation(value = "Delete a rules")
	@DeleteMapping("/rules/{id}")
	public ResponseEntity<Void> deleteRules(@PathVariable Long id) {
		logger.info("Request to delete a rules");
		rulesService.deleteRules(id);
		return ResponseEntity.noContent()
				.headers(HeaderUtil.createEntityDeletionAlert("rules", false, "rules", id.toString())).build();
	}

	
	@ApiOperation(value = "Search a rule by id")
	@GetMapping("/rules/{id}")
	public ResponseEntity<Rules> getRules(@PathVariable Long id) {
		logger.info("Getting rule by id: " + id);

		Map<String, String> ruleObj = new HashMap<>();

		try {
			Rules rules = null;
			ruleObj.put("id", String.valueOf(id));
			List<Rules> ruleList;

			ruleList = rulesService.searchRules(ruleObj);
			if (ruleList.size() > 0) {
				rules = ruleList.get(0);
			}
			return ResponseEntity.status(HttpStatus.OK).body(rules);
		} catch (NegativeIdException e) {
			logger.error("Search rule failed. NegativeIdException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.NEGATIVE_ID_NOT_ALLOWED.value()).body(null);
		} catch (Exception e) {
			logger.error("Search rule failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}

	}
	
	@ApiOperation(value = "Search a rule by name")
	@GetMapping("/rules/name/{name}")
	public ResponseEntity<Rules> getRulesByName(@PathVariable String name) {
		logger.info("Getting rule by name: " + name);
		try {
			Rules rules = rulesService.getRulesByName(name);
			return ResponseEntity.status(HttpStatus.OK).body(rules);
		} catch (NegativeIdException e) {
			logger.error("Search rule failed. NegativeIdException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.NEGATIVE_ID_NOT_ALLOWED.value()).body(null);
		} catch (Exception e) {
			logger.error("Search rule failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}
	}
}