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
import com.synectiks.procurement.business.service.CommitteeService;
import com.synectiks.procurement.config.BusinessValidationCodes;
import com.synectiks.procurement.config.Constants;
import com.synectiks.procurement.domain.Committee;
import com.synectiks.procurement.web.rest.errors.DataNotFoundException;
import com.synectiks.procurement.web.rest.errors.IdNotFoundException;
import com.synectiks.procurement.web.rest.errors.NegativeIdException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "Committee APIs")
@RestController
@RequestMapping("/api")
public class CommitteeController {
	private static final Logger logger = LoggerFactory.getLogger(CommitteeController.class);

	@Autowired
	private CommitteeService committeeService;

	@ApiOperation(value = "Create a new committee")
	@PostMapping("/committee")
	public ResponseEntity<Committee> addCommittee(@RequestBody ObjectNode obj) {
		logger.info("Request to add New committee");
		try {
			Committee committee = committeeService.addCommittee(obj);
			return ResponseEntity.status(HttpStatus.OK).body(committee);
		} catch (Exception e) {
			logger.error("Add committee failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}
		
	}

	@ApiOperation(value = "Update an existing committee")
	@PutMapping("/committee")
	public ResponseEntity<Committee> updateCommittee(@RequestBody ObjectNode obj){
		logger.info("Request to update a committee");
		try {
			Committee committee = committeeService.updateCommittee(obj);
			return ResponseEntity.status(HttpStatus.OK).body(committee);
		} 
		catch (NegativeIdException e) {
			logger.error("Update committee failed. NegativeIdException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.NEGATIVE_ID_NOT_ALLOWED.value()).body(null);
		} catch (IdNotFoundException e) {
			logger.error("Update committee failed. IdNotFoundException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.ID_NOT_FOUND.value()).body(null);
		}catch (DataNotFoundException e) {
			logger.error("Update committee failed. DataNotFoundException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.DATA_NOT_FOUND.value()).body(null);
		}
		catch (Exception e) {
			logger.error("Update committee failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}
			

	}
	@ApiOperation(value = "Search committee")
	@GetMapping("/committee")
	public ResponseEntity<List<Committee>> searchCommittee(@RequestParam Map<String, String> requestObj) {
		logger.info("Request to get list of committee on given filter criteria");
		try {
			List<Committee> list = committeeService.searchCommittee(requestObj);
			return ResponseEntity.status(HttpStatus.OK).body(list);
		}catch (NegativeIdException e) {
			logger.error("Update committee failed. NegativeIdException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.NEGATIVE_ID_NOT_ALLOWED.value()).body(null);
		} 
		catch (Exception e) {
			logger.error("Search committee failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}
	
	}

	@ApiOperation(value = "Delete a committee")
	@DeleteMapping("/committee/{id}")
	public ResponseEntity<Boolean> deleteCommittee(@PathVariable Long id) {
		logger.info("Request to delete a committee");
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode obj = mapper.createObjectNode();
		obj.put("id", id);
		obj.put("status", Constants.STATUS_DEACTIVE);
		try {
			Committee	committee = committeeService.updateCommittee(obj);
			 if(Constants.STATUS_DEACTIVE.equalsIgnoreCase(committee.getStatus())){
					return ResponseEntity.status(HttpStatus.OK).body(Boolean.TRUE);
				}else {
					return ResponseEntity.status(BusinessValidationCodes.DELETION_FAILED.value()).body(Boolean.FALSE);
				}
		} catch (NegativeIdException e) {
			logger.error("Delete committee failed. NegativeIdException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.NEGATIVE_ID_NOT_ALLOWED.value()).body(null);
		} catch (IdNotFoundException e) {
			logger.error("Delete committee failed. IdNotFoundException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.ID_NOT_FOUND.value()).body(null);
		}catch (DataNotFoundException e) {
			logger.error("Delete committee failed. DataNotFoundException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.DATA_NOT_FOUND.value()).body(null);
		}
		catch (Exception e) {
			logger.error("Delete committee failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}
	}

	@ApiOperation(value = "Get a committee by id")
	@GetMapping("/committee/{id}")
	public ResponseEntity<Committee> getCommittee(@PathVariable Long id) {
		logger.info("Getting committee by id: " + id);
		Map<String, String> cmm = new HashMap<>();
		try {
			Committee committee = null;
			cmm.put("id", String.valueOf(id));
			List<Committee> cmmList = committeeService.searchCommittee(cmm);
			if(cmmList.size() > 0) {
				committee = cmmList.get(0);
			}
			return ResponseEntity.status(HttpStatus.OK).body(committee);
		}catch (NegativeIdException e) {
			logger.error("Getting committee by id failed. NegativeIdException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.NEGATIVE_ID_NOT_ALLOWED.value()).body(null);
		}catch (Exception e) {
			logger.error("Getting committee by id failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}
 }

}