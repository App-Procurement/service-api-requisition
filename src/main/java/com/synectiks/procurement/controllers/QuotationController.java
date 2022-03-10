package com.synectiks.procurement.controllers;

import java.net.URISyntaxException;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.synectiks.procurement.business.service.QuotationService;
import com.synectiks.procurement.config.BusinessValidationCodes;
import com.synectiks.procurement.config.Constants;
import com.synectiks.procurement.domain.Quotation;
import com.synectiks.procurement.web.rest.errors.DataNotFoundException;
import com.synectiks.procurement.web.rest.errors.IdNotFoundException;
import com.synectiks.procurement.web.rest.errors.NegativeIdException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "Quotation APIs")
@RestController
@RequestMapping("/api")
public class QuotationController {
	private static final Logger logger = LoggerFactory.getLogger(QuotationController.class);

	@Autowired
	private QuotationService quotationService;

	@ApiOperation(value = "Create a new quotation")
	@PostMapping("/quotation")
	public ResponseEntity<Quotation> addQuotation(@RequestBody ObjectNode obj) {
		Quotation quotation;
		try {
			quotation = quotationService.addQuotation(obj);
			return ResponseEntity.status(HttpStatus.OK).body(quotation);
		} catch (NumberFormatException e) {
			logger.error("Add quotation failed. NumberFormatException ", e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (NegativeIdException e) {
			logger.error("Add quotation failed. NegativeIdException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.NEGATIVE_ID_NOT_ALLOWED.value()).body(null);
		} catch (IdNotFoundException e) {
			logger.error("Add quotation failed. IdNotFoundException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.ID_NOT_FOUND.value()).body(null);
		} catch (DataNotFoundException e) {
			logger.error("Add quotation failed. DataNotFoundException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.DATA_NOT_FOUND.value()).body(null);
		} catch (Exception e) {
			logger.error("Add quotation failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}
	}

	@ApiOperation(value = "Update an existing quotation")
	@PutMapping("/quotation")
	public ResponseEntity<Quotation> updateQuotation(@RequestBody ObjectNode obj)
			throws JSONException, URISyntaxException {
		try {
			Quotation quotation = quotationService.updateQuotation(obj);
			return ResponseEntity.status(HttpStatus.OK).body(quotation);
		} catch (NumberFormatException e) {
			logger.error("Update quotation failed. NumberFormatException ", e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (NegativeIdException e) {
			logger.error("Update quotation failed. NegativeIdException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.NEGATIVE_ID_NOT_ALLOWED.value()).body(null);
		} catch (IdNotFoundException e) {
			logger.error("Update quotation failed. IdNotFoundException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.ID_NOT_FOUND.value()).body(null);
		} catch (DataNotFoundException e) {
			logger.error("Update quotation failed. DataNotFoundException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.DATA_NOT_FOUND.value()).body(null);
		} catch (Exception e) {
			logger.error("Update quotation failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}
	}

	@ApiOperation(value = "Search quotation")
	@GetMapping("/quotation")
	public ResponseEntity<List<Quotation>> searchQuotation(@RequestParam Map<String, String> requestObj) {
		logger.info("Request to search quotation on given filter criteria");
		try {
			List<Quotation> list = quotationService.searchQuotation(requestObj);
			return ResponseEntity.status(HttpStatus.OK).body(list);
		} catch (NegativeIdException e) {
			logger.error("Search quotation failed. NegativeIdException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.NEGATIVE_ID_NOT_ALLOWED.value()).body(null);
		} catch (Exception e) {
			logger.error("Search quotation failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}
	}

	@ApiOperation(value = "Delete a quotation")
	@DeleteMapping("/quotation/{id}")
	public ResponseEntity<Boolean> deletequotation(@PathVariable Long id) {
		logger.info("Request to delete a quotation");
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode obj = mapper.createObjectNode();
		obj.put("id", id);
		obj.put("status", Constants.STATUS_DEACTIVE);
		try {
			Quotation bu = quotationService.updateQuotation(obj);
			if (Constants.STATUS_DEACTIVE.equalsIgnoreCase(bu.getStatus())) {
				return ResponseEntity.status(HttpStatus.OK).body(Boolean.TRUE);
			} else {
				return ResponseEntity.status(BusinessValidationCodes.DELETION_FAILED.value()).body(Boolean.FALSE);
			}
		} catch (NegativeIdException e) {
			logger.error("Delete quotation failed. NegativeIdException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.NEGATIVE_ID_NOT_ALLOWED.value()).body(null);
		} catch (IdNotFoundException e) {
			logger.error("Delete quotation failed. IdNotFoundException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.ID_NOT_FOUND.value()).body(null);
		} catch (DataNotFoundException e) {
			logger.error("Delete quotation failed. DataNotFoundException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.DATA_NOT_FOUND.value()).body(null);
		} catch (Exception e) {
			logger.error("Delete quotation failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}

	}

//	@ApiOperation(value = "Delete a quotation")
//	@DeleteMapping("/quotation/{id}")
//	public ResponseEntity<Boolean> deleteQuotation(@PathVariable Long id) {
//
//		try {
//			boolean delQuotation = quotationService.deleteQuotation(id);
//			if (delQuotation) {
//				return ResponseEntity.status(HttpStatus.OK).body(delQuotation);
//			} else {
//				return ResponseEntity.status(HttpStatus.OK).body(!delQuotation);
//			}
//		} catch (NegativeIdException e) {
//			logger.error("Delete quotation failed. NegativeIdException: ", e.getMessage());
//			return ResponseEntity.status(BusinessValidationCodes.NEGATIVE_ID_NOT_ALLOWED.value()).body(null);
//		} catch (IdNotFoundException e) {
//			logger.error("Delete quotation failed. IdNotFoundException: ", e.getMessage());
//			return ResponseEntity.status(BusinessValidationCodes.ID_NOT_FOUND.value()).body(null);
//		} catch (DataNotFoundException e) {
//			logger.error("Delete quotation failed. DataNotFoundException: ", e.getMessage());
//			return ResponseEntity.status(BusinessValidationCodes.DATA_NOT_FOUND.value()).body(null);
//		} catch (Exception e) {
//			logger.error("Delete quotation failed. Exception: ", e);
//			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
//		}
//
//	}

	@ApiOperation(value = "Search a quotation by id")
	@GetMapping("/quotation/{id}")
	public ResponseEntity<Quotation> getQuotation(@PathVariable Long id) {
		logger.info("Getting quotation by id: " + id);
		Map<String, String> quoObj = new HashMap<>();
		try {
			Quotation quotation = null;
			quoObj.put("id", String.valueOf(id));
			List<Quotation> quoList;

			quoList = quotationService.searchQuotation(quoObj);
			if (quoList.size() > 0) {
				quotation = quoList.get(0);
			}
			return ResponseEntity.status(HttpStatus.OK).body(quotation);
		} catch (NegativeIdException e) {
			logger.error("Search vendor failed. NegativeIdException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.NEGATIVE_ID_NOT_ALLOWED.value()).body(null);
		} catch (Exception e) {
			logger.error("Search vendor failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}

	}
}
