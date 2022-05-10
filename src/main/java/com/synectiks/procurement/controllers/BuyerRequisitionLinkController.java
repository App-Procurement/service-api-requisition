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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.synectiks.procurement.business.service.BuyerRequisitionLinkService;
import com.synectiks.procurement.config.BusinessValidationCodes;
import com.synectiks.procurement.domain.BuyerRequisitionLink;
import com.synectiks.procurement.web.rest.errors.DataNotFoundException;
import com.synectiks.procurement.web.rest.errors.IdNotFoundException;
import com.synectiks.procurement.web.rest.errors.NegativeIdException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "Buyer Requisition Link APIs")
@RestController
@RequestMapping("/api")
public class BuyerRequisitionLinkController {
	private static final Logger logger = LoggerFactory.getLogger(BuyerRequisitionLinkController.class);

	@Autowired
	BuyerRequisitionLinkService buyerRequisitionLinkService;

	@ApiOperation(value = "Create a new buyerRequisitionLink")
	@PostMapping("/buyer/{requisitionId}")
	public ResponseEntity<List<BuyerRequisitionLink>> addbuyerRequisitionLink(@RequestBody ObjectNode obj,
			@PathVariable Integer requisitionId) {
		logger.info("Request to add a buyerRequisitionLink");
		try {
			List<BuyerRequisitionLink> buyerRequisitionLink = buyerRequisitionLinkService.addBuyerRequisitionLink(obj,
					requisitionId);
			return ResponseEntity.status(HttpStatus.OK).body(buyerRequisitionLink);
		} catch (Exception e) {
			logger.error("Add buyerRequisitionLink failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}
	}

	@ApiOperation(value = "Search buyerRequisitionLink")
	@GetMapping("/buyerRequisitionLink")
	public ResponseEntity<List<BuyerRequisitionLink>> searchbuyerRequisitionLink(
			@RequestParam Map<String, String> requestObj) {
		logger.info("Request to search buyerRequisitionLink on given filter criteria");
		List<BuyerRequisitionLink> list;
		try {
			list = buyerRequisitionLinkService.searchBuyerRequisitionLink(requestObj);
			return ResponseEntity.status(HttpStatus.OK).body(list);
		} catch (NegativeIdException e) {
			logger.error("Search buyerRequisitionLink failed. NegativeIdException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.NEGATIVE_ID_NOT_ALLOWED.value()).body(null);
		} catch (Exception e) {
			logger.error("Search buyerRequisitionLink failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}
	}

	@ApiOperation(value = "Delete a buyerRequisitionLink")
	@DeleteMapping("/buyerRequisitionLink/{id}")
	public ResponseEntity<Boolean> deletebuyerRequisitionLink(@PathVariable Long id) {
		logger.info("Request to delete a buyerRequisitionLink");
		try {
			boolean delbuyerRequisitionLink = buyerRequisitionLinkService.deleteBuyerRequisitionLink(id);
			if (delbuyerRequisitionLink) {
				return ResponseEntity.status(HttpStatus.OK).body(Boolean.TRUE);
			} else {
				return ResponseEntity.status(BusinessValidationCodes.DELETION_FAILED.value()).body(Boolean.FALSE);
			}
		} catch (NegativeIdException e) {
			logger.error("Delete buyerRequisitionLink failed. NegativeIdException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.NEGATIVE_ID_NOT_ALLOWED.value()).body(null);
		} catch (IdNotFoundException e) {
			logger.error("Delete buyerRequisitionLink failed. IdNotFoundException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.ID_NOT_FOUND.value()).body(null);
		} catch (DataNotFoundException e) {
			logger.error("Delete buyerRequisitionLink failed. DataNotFoundException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.DATA_NOT_FOUND.value()).body(null);
		} catch (Exception e) {
			logger.error("Delete buyerRequisitionLink failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}
	}

}
