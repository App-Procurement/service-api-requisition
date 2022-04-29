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
import com.synectiks.procurement.business.service.ApprovalRulesService;
import com.synectiks.procurement.config.BusinessValidationCodes;
import com.synectiks.procurement.config.Constants;
import com.synectiks.procurement.domain.ApprovalRules;
import com.synectiks.procurement.web.rest.errors.DataNotFoundException;
import com.synectiks.procurement.web.rest.errors.IdNotFoundException;
import com.synectiks.procurement.web.rest.errors.NegativeIdException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "ApprovalRules APIs")
@RestController
@RequestMapping("/api")
public class ApprovalRulesController {
	private static final Logger logger = LoggerFactory.getLogger(ApprovalRulesController.class);

	@Autowired
	ApprovalRulesService approvalRulesService;

	@ApiOperation(value = "Create a new approvalRules")
	@PostMapping("/approvalRules")
	public ResponseEntity<ApprovalRules> addApprovalRules(@RequestBody ObjectNode obj) {
		logger.info("Request to add a approvalRules");
		try {

			ApprovalRules approvalRules = approvalRulesService.addApprovalRules(obj);
			return ResponseEntity.status(HttpStatus.OK).body(approvalRules);
		} catch (Exception e) {
			logger.error("Add approvalRules failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}
	}

	@ApiOperation(value = "Update an existing approvalRules")
	@PutMapping("/approvalRules")
	public ResponseEntity<ApprovalRules> updateApprovalRules(@RequestBody ObjectNode obj) {
		logger.info("Request to update a approvalRules");
		try {
			ApprovalRules approvalRules = approvalRulesService.updateApprovalRules(obj);
			return ResponseEntity.status(HttpStatus.OK).body(approvalRules);
		} catch (NegativeIdException e) {
			logger.error("Update approvalRules failed. NegativeIdException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.NEGATIVE_ID_NOT_ALLOWED.value()).body(null);
		} catch (IdNotFoundException e) {
			logger.error("Update approvalRules failed. IdNotFoundException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.ID_NOT_FOUND.value()).body(null);
		} catch (DataNotFoundException e) {
			logger.error("Update approvalRules failed. DataNotFoundException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.DATA_NOT_FOUND.value()).body(null);
		} catch (Exception e) {
			logger.error("Update approvalRules failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}

	}

	@ApiOperation(value = "Search approvalRules")
	@GetMapping("/approvalRules")
	public ResponseEntity<List<ApprovalRules>> searchApprovalRules(@RequestParam Map<String, String> requestObj) {
		logger.info("Request to search approvalRules on given filter criteria");
		try {
			List<ApprovalRules> list = approvalRulesService.searchApprovalRules(requestObj);
			return ResponseEntity.status(HttpStatus.OK).body(list);
		} catch (NegativeIdException e) {
			logger.error("Search approvalRules failed. NegativeIdException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.NEGATIVE_ID_NOT_ALLOWED.value()).body(null);
		} catch (Exception e) {
			logger.error("Search approvalRules failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}

	}

	@ApiOperation(value = "Delete an approvalRules")
	@DeleteMapping("/approvalRules/{id}")
	public ResponseEntity<Boolean> deleteApprovalRules(@PathVariable Long id) {
		logger.info("Request to delete a approvalRules");
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode obj = mapper.createObjectNode();
		obj.put("id", id);
		obj.put("status", Constants.STATUS_DEACTIVE);
		try {
			ApprovalRules bu = approvalRulesService.updateApprovalRules(obj);
			if (Constants.STATUS_DEACTIVE.equalsIgnoreCase(bu.getStatus())) {
				return ResponseEntity.status(HttpStatus.OK).body(Boolean.TRUE);
			} else {
				return ResponseEntity.status(BusinessValidationCodes.DELETION_FAILED.value()).body(Boolean.FALSE);
			}
		} catch (NegativeIdException e) {
			logger.error("Delete approvalRules failed. NegativeIdException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.NEGATIVE_ID_NOT_ALLOWED.value()).body(null);
		} catch (IdNotFoundException e) {
			logger.error("Delete approvalRules failed. IdNotFoundException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.ID_NOT_FOUND.value()).body(null);
		} catch (DataNotFoundException e) {
			logger.error("Delete approvalRules failed. DataNotFoundException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.DATA_NOT_FOUND.value()).body(null);
		} catch (Exception e) {
			logger.error("Delete approvalRules failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}

	}

	@ApiOperation(value = "Get an approvalRules by id")
	@GetMapping("/approvalRules/{id}")
	public ResponseEntity<ApprovalRules> getApprovalRules(@PathVariable Long id) {
		logger.info("Getting approvalRules by id: " + id);
		Map<String, String> buy = new HashMap<>();
		try {
			ApprovalRules approvalRules = null;
			buy.put("id", String.valueOf(id));
			List<ApprovalRules> buyList = approvalRulesService.searchApprovalRules(buy);
			if (buyList.size() > 0) {
				approvalRules = buyList.get(0);
			}
			return ResponseEntity.status(HttpStatus.OK).body(approvalRules);
		} catch (NegativeIdException e) {
			logger.error("Getting approvalRules by id failed. NegativeIdException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.NEGATIVE_ID_NOT_ALLOWED.value()).body(null);
		} catch (Exception e) {
			logger.error("Getting approvalRules by id failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}
	}

}
