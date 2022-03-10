package com.synectiks.procurement.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.synectiks.procurement.business.service.CommitteeMembersService;
import com.synectiks.procurement.config.BusinessValidationCodes;
import com.synectiks.procurement.domain.CommitteeMember;
import com.synectiks.procurement.web.rest.errors.DataNotFoundException;
import com.synectiks.procurement.web.rest.errors.IdNotFoundException;
import com.synectiks.procurement.web.rest.errors.NegativeIdException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "Committee members APIs")
@RestController
@RequestMapping("/api")
public class CommitteeMembersController {
	private static final Logger logger = LoggerFactory.getLogger(CommitteeMembersController.class);

	@Autowired
	private CommitteeMembersService committeeMembersService;

	@ApiOperation(value = "Create a new committee members")
	@RequestMapping(value = "/committeeMembers", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CommitteeMember> addCommitteeMembers(@RequestBody ObjectNode objNode,
			@RequestParam(name = "file", required = false) MultipartFile file) {
		logger.info("Request to add new committee member");
		CommitteeMember committeeMember = null;

		try {
			String obj = objNode.toPrettyString();
			committeeMember = committeeMembersService.addCommitteeMember(obj, file);
			return ResponseEntity.status(HttpStatus.OK).body(committeeMember);
		} catch (JSONException e) {
			logger.error("Add committee member failed. JSONException: ", e);
			return ResponseEntity.status(BusinessValidationCodes.JSON_EXCEPTION.value()).body(null);
		} catch (IOException e) {
			logger.error("Add committee member failed. IOException: ", e);
			return ResponseEntity.status(BusinessValidationCodes.IO_EXCEPTION.value()).body(null);
		} catch (Exception e) {
			logger.error("Update committee member failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}

	}

	@ApiOperation(value = "Update an existing committee members")
	@RequestMapping(value = "/committeeMembers", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CommitteeMember> updateCommitteeMembers(@RequestBody ObjectNode objNode,
			@RequestParam(name = "file", required = false) MultipartFile file) throws IOException, JSONException {
		logger.info("Request to upde committee members");
		CommitteeMember committeeMember = null;
		try {
			String obj = objNode.toPrettyString();
			committeeMember = committeeMembersService.updateCommitteeMembers(obj, file);
			return ResponseEntity.status(HttpStatus.OK).body(committeeMember);
		} catch (JSONException e) {
			logger.error("Update committee members failed. JSONException: ", e);
			return ResponseEntity.status(BusinessValidationCodes.JSON_EXCEPTION.value()).body(null);
		} catch (IOException e) {
			logger.error("Update committee members failed. IOException: ", e);
			return ResponseEntity.status(BusinessValidationCodes.IO_EXCEPTION.value()).body(null);
		} catch (NegativeIdException e) {
			logger.error("Update committee members failed. NegativeIdException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.NEGATIVE_ID_NOT_ALLOWED.value()).body(null);
		} catch (IdNotFoundException e) {
			logger.error("Update committee members failed. IdNotFoundException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.ID_NOT_FOUND.value()).body(null);
		} catch (DataNotFoundException e) {
			logger.error("Update committee members failed. DataNotFoundException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.DATA_NOT_FOUND.value()).body(null);
		} catch (Exception e) {
			logger.error("Update committee members failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}
	}

	@ApiOperation(value = "Search committee members")
	@GetMapping("/committeeMembers")
	public ResponseEntity<List<CommitteeMember>> searchCommitteeMembers(@RequestParam Map<String, String> requestObj) {
		logger.info("Request to get list of committee members on given filter criteria");

		try {
			List<CommitteeMember> list;
			list = committeeMembersService.searchCommitteeMembers(requestObj);
			return ResponseEntity.status(HttpStatus.OK).body(list);
		} catch (IOException e) {
			logger.error("Update committee members failed. IOException: ", e);
			return ResponseEntity.status(BusinessValidationCodes.IO_EXCEPTION.value()).body(null);
		} catch (NegativeIdException e) {
			logger.error("Search committee members failed. NegativeIdException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.NEGATIVE_ID_NOT_ALLOWED.value()).body(null);
		} catch (Exception e) {
			logger.error("Update committee members failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}
	}

	@ApiOperation(value = "Search a committee members by id")
	@GetMapping("/committeeMembers/{id}")
	public ResponseEntity<List<CommitteeMember>> getCommitteeMembers(@PathVariable Map<String, String> id) {
		logger.info("Getting committee members by id: " + id);
		List<CommitteeMember> committeeMembers;
		try {
			committeeMembers = committeeMembersService.searchCommitteeMembers(id);
			return ResponseEntity.status(HttpStatus.OK).body(committeeMembers);
		} catch (IOException e) {
			logger.error("Update committee members failed. IOException: ", e);
			return ResponseEntity.status(BusinessValidationCodes.IO_EXCEPTION.value()).body(null);
		} catch (NegativeIdException e) {
			logger.error("Search committee members failed. NegativeIdException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.NEGATIVE_ID_NOT_ALLOWED.value()).body(null);
		} catch (Exception e) {
			logger.error("Update committee members failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}

	}

}