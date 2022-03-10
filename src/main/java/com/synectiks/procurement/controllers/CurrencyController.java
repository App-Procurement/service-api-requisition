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

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.synectiks.procurement.business.service.CurrencyService;
import com.synectiks.procurement.config.BusinessValidationCodes;
import com.synectiks.procurement.domain.Committee;
import com.synectiks.procurement.domain.Currency;
import com.synectiks.procurement.web.rest.errors.DataNotFoundException;
import com.synectiks.procurement.web.rest.errors.IdNotFoundException;
import com.synectiks.procurement.web.rest.errors.NegativeIdException;

import io.github.jhipster.web.util.HeaderUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "Currency APIs")
@RestController
@RequestMapping("/api")
//@Api(value = "/api", tags = "Currency Management")
public class CurrencyController {
	private static final Logger logger = LoggerFactory.getLogger(CurrencyController.class);

	@Autowired
	private CurrencyService currencyService;

	@ApiOperation(value = "Create a new currency")
	@PostMapping("/currency")
	public ResponseEntity<Currency> addCurrency(@RequestBody ObjectNode obj){
		logger.info("Request to add currency");
		try {
			Currency currency = currencyService.addCurrency(obj);
			return ResponseEntity.status(HttpStatus.OK).body(currency);
		} catch (Exception e) {
			logger.error("Add currency failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}
			
	}

	@ApiOperation(value = "Update an existing currency")
	@PutMapping("/currency")
	public ResponseEntity<Currency> updateCurrency(@RequestBody ObjectNode obj){
		logger.info("Request to updating currency");
		try {
			Currency currency = currencyService.updateCurrency(obj);
			return ResponseEntity.status(HttpStatus.OK).body(currency);
		} catch (NegativeIdException e) {
			logger.error("Update currency failed. NegativeIdException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.NEGATIVE_ID_NOT_ALLOWED.value()).body(null);
		} catch (IdNotFoundException e) {
			logger.error("Update currency failed. IdNotFoundException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.ID_NOT_FOUND.value()).body(null);
		}catch (DataNotFoundException e) {
			logger.error("Update currency failed. DataNotFoundException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.DATA_NOT_FOUND.value()).body(null);
		}
		catch (Exception e) {
			logger.error("Update currency failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}
	}

	@ApiOperation(value = "Search currency")
	@GetMapping("/currency")
	public ResponseEntity<List<Currency>> searchCurrency(@RequestParam Map<String, String> requestObj) {
		logger.info("Request to get currency on given filter criteria");
		try {
			List<Currency> list = currencyService.searchCurrency(requestObj);
			return ResponseEntity.status(HttpStatus.OK).body(list);
		} catch (NegativeIdException e) {
			logger.error("Update currency failed. NegativeIdException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.NEGATIVE_ID_NOT_ALLOWED.value()).body(null);
		} 
		catch (Exception e) {
			logger.error("Search currency failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}
			
	}

	@ApiOperation(value = "Delete a currency")
	@DeleteMapping("/currency/{id}")
	public ResponseEntity<Boolean> deleteCurrency(@PathVariable Long id) {
		try {
			boolean dlCurrency = currencyService.deleteCurrency(id);
			if (dlCurrency) {
				return ResponseEntity.status(HttpStatus.OK).body(dlCurrency);
			} else {
				return ResponseEntity.status(HttpStatus.OK).body(dlCurrency);
			} 
			
		} catch (NegativeIdException e) {
			logger.error("Delete currency failed. NegativeIdException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.NEGATIVE_ID_NOT_ALLOWED.value()).body(null);
		} catch (IdNotFoundException e) {
			logger.error("Delete currency failed. IdNotFoundException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.ID_NOT_FOUND.value()).body(null);
		} catch (DataNotFoundException e) {
			logger.error("Delete currency failed. DataNotFoundException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.DATA_NOT_FOUND.value()).body(null);
		} catch (Exception e) {
			logger.error("Delete currency failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}
	}

	@ApiOperation(value = "Search a currency by id")
	@GetMapping("/currency/{id}")
	public ResponseEntity<Currency> getCurrency(@PathVariable Long id) {
		logger.info("Getting currency by id: " + id);
		Map<String, String> cur = new HashMap<>();
		try {
			Currency currency = null;
			cur.put("id", String.valueOf(id));
			List<Currency> curList = currencyService.searchCurrency(cur);
			if(curList.size() > 0) {
				currency = curList.get(0);
			}
			return ResponseEntity.status(HttpStatus.OK).body(currency);
		}catch (NegativeIdException e) {
			logger.error("Getting currency by id failed. NegativeIdException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.NEGATIVE_ID_NOT_ALLOWED.value()).body(null);
		}catch (Exception e) {
			logger.error("Getting currency by id failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}
 }


}
