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
import com.synectiks.procurement.business.service.BuyerService;
import com.synectiks.procurement.config.BusinessValidationCodes;
import com.synectiks.procurement.config.Constants;
import com.synectiks.procurement.domain.Buyer;
import com.synectiks.procurement.web.rest.errors.DataNotFoundException;
import com.synectiks.procurement.web.rest.errors.IdNotFoundException;
import com.synectiks.procurement.web.rest.errors.NegativeIdException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "Buyer APIs")
@RestController
@RequestMapping("/api")
public class BuyerController {
	private static final Logger logger = LoggerFactory.getLogger(BuyerController.class);

	@Autowired
	BuyerService buyerService;

	@ApiOperation(value = "Create a new buyer")
	@PostMapping("/buyer")
	public ResponseEntity<Buyer> addBuyer(@RequestBody ObjectNode obj)  {
		logger.info("Request to add a buyer");
		try {
		
		Buyer  buyer = buyerService.addBuyer(obj);
		return ResponseEntity.status(HttpStatus.OK).body(buyer);
		}
		catch (Exception e) {
			logger.error("Add buyer failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}
	}

	@ApiOperation(value = "Update an existing buyer")
	@PutMapping("/buyer")
	public ResponseEntity<Buyer> updateBuyer(@RequestBody ObjectNode obj){
		logger.info("Request to update a buyer");
		try {
			Buyer buyer = buyerService.updateBuyer(obj);
			return ResponseEntity.status(HttpStatus.OK).body(buyer);
		}
		catch (NegativeIdException e) {
			logger.error("Update buyer failed. NegativeIdException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.NEGATIVE_ID_NOT_ALLOWED.value()).body(null);
		} catch (IdNotFoundException e) {
			logger.error("Update buyer failed. IdNotFoundException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.ID_NOT_FOUND.value()).body(null);
		}catch (DataNotFoundException e) {
			logger.error("Update buyer failed. DataNotFoundException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.DATA_NOT_FOUND.value()).body(null);
		}
		catch (Exception e) {
			logger.error("Update buyer failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}
			
	}
	
	@ApiOperation(value = "Search buyer")
	@GetMapping("/buyer")
	public ResponseEntity<List<Buyer>> searchBuyer(@RequestParam Map<String, String> requestObj) {
		logger.info("Request to search buyer on given filter criteria");
		try {
			List<Buyer> list = buyerService.searchBuyer(requestObj);
			return ResponseEntity.status(HttpStatus.OK).body(list);
		}
		catch (NegativeIdException e) {
			logger.error("Search buyer failed. NegativeIdException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.NEGATIVE_ID_NOT_ALLOWED.value()).body(null);
		}
		catch (Exception e) {
			logger.error("Search buyer failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}
			
	}

	@ApiOperation(value = "Delete a buyer")
	@DeleteMapping("/buyer/{id}")
	public ResponseEntity<Boolean> deleteBuyer(@PathVariable Long id) {
		logger.info("Request to delete a buyer");
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode obj = mapper.createObjectNode();
		obj.put("id", id);
		obj.put("status", Constants.STATUS_DEACTIVE);
		try {
		Buyer bu=buyerService.updateBuyer(obj);
			if(Constants.STATUS_DEACTIVE.equalsIgnoreCase(bu.getStatus())){
				return ResponseEntity.status(HttpStatus.OK).body(Boolean.TRUE);
			}else {
				return ResponseEntity.status(BusinessValidationCodes.DELETION_FAILED.value()).body(Boolean.FALSE);
			}
		} 
		catch (NegativeIdException e) {
			logger.error("Delete buyer failed. NegativeIdException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.NEGATIVE_ID_NOT_ALLOWED.value()).body(null);
		} catch (IdNotFoundException e) {
			logger.error("Delete buyer failed. IdNotFoundException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.ID_NOT_FOUND.value()).body(null);
		} catch (DataNotFoundException e) {
			logger.error("Delete buyer failed. DataNotFoundException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.DATA_NOT_FOUND.value()).body(null);
		}
		catch (Exception e) {
			logger.error("Delete buyer failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}
			
	}

	@ApiOperation(value = "Get a buyer by id")
	@GetMapping("/buyer/{id}")
	public ResponseEntity<Buyer> getBuyer(@PathVariable Long id) {
		logger.info("Getting buyer by id: " + id);
		Map<String, String> buy = new HashMap<>();
		try {
			Buyer buyer = null;
			buy.put("id", String.valueOf(id));
			List<Buyer> buyList = buyerService.searchBuyer(buy);
			if(buyList.size() > 0) {
				buyer = buyList.get(0);
			}
			return ResponseEntity.status(HttpStatus.OK).body(buyer);
		}catch (NegativeIdException e) {
			logger.error("Getting buyer by id failed. NegativeIdException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.NEGATIVE_ID_NOT_ALLOWED.value()).body(null);
		}catch (Exception e) {
			logger.error("Getting buyer by id failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}
  }
	
}

