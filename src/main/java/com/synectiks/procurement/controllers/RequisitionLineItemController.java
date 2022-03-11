package com.synectiks.procurement.controllers;

import java.io.IOException;
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
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.synectiks.procurement.business.service.RequisitionLineItemService;
import com.synectiks.procurement.config.BusinessValidationCodes;
import com.synectiks.procurement.config.Constants;
import com.synectiks.procurement.domain.RequisitionLineItem;
import com.synectiks.procurement.web.rest.errors.DataNotFoundException;
import com.synectiks.procurement.web.rest.errors.IdNotFoundException;
import com.synectiks.procurement.web.rest.errors.NegativeIdException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "Requisition line item APIs")
@RestController
@RequestMapping("/api")
public class RequisitionLineItemController {
	private static final Logger logger = LoggerFactory.getLogger(RequisitionLineItemController.class);

	@Autowired
	private RequisitionLineItemService requisitionLineItemService;

	@ApiOperation(value = "Create a new requisition line item")
	@PostMapping("/requisitionLineItem")
	public ResponseEntity<RequisitionLineItem> addRequisitionLineItem(
			@RequestParam(name = "requisitionLineItemFile", required = false) MultipartFile[] requisitionLineItemFile,
			@RequestParam String obj) throws IOException {
		logger.info("Request to add a requisition line item");
		RequisitionLineItem requisitionLineItem;
		try {
			/* String obj = objNode.toPrettyString(); */
			requisitionLineItem = requisitionLineItemService.addRequisitionLineItem(obj, requisitionLineItemFile);
			return ResponseEntity.status(HttpStatus.OK).body(requisitionLineItem);
		} catch (IOException e) {
			logger.error("Add requisition line item failed. IOException: ", e);
			return ResponseEntity.status(BusinessValidationCodes.IO_EXCEPTION.value()).body(null);
		} catch (Exception e) {
			logger.error("Add requisition line item failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}
	}

	@ApiOperation(value = "Update an existing requisition line item")
	@PutMapping("/requisitionLineItem")
	public ResponseEntity<RequisitionLineItem> updateRequisitionLineItem(
			@RequestParam(name = "requisitionLineItemFile", required = false) MultipartFile[] requisitionLineItemFile,
			@RequestParam String obj) throws IOException {
		logger.info("Request to update a requsition");
		try {
//			String obj = objNode.toPrettyString();
			RequisitionLineItem requisitionLineItem = requisitionLineItemService.updateRequisitionLineItem(obj,
					requisitionLineItemFile);
			return ResponseEntity.status(HttpStatus.OK).body(requisitionLineItem);
		} catch (IOException e) {
			logger.error("Update requisition line item failed. IOException: ", e);
			return ResponseEntity.status(BusinessValidationCodes.IO_EXCEPTION.value()).body(null);
		} catch (NegativeIdException e) {
			logger.error("Update requisition line item failed. NegativeIdException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.NEGATIVE_ID_NOT_ALLOWED.value()).body(null);
		} catch (IdNotFoundException e) {
			logger.error("Update requisition line item failed. IdNotFoundException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.ID_NOT_FOUND.value()).body(null);
		} catch (DataNotFoundException e) {
			logger.error("Update requisition line item failed. DataNotFoundException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.DATA_NOT_FOUND.value()).body(null);
		} catch (Exception e) {
			logger.error("Update requisition line item failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}
	}

	@ApiOperation(value = "Search requisitions line item")
	@GetMapping("/requisitionLineItem")
	public ResponseEntity<List<RequisitionLineItem>> searchRequisitionLineItem(
			@RequestParam Map<String, String> requestObj) {
		logger.info("Request to get requisition line item on given filter criteria");
		try {
			List<RequisitionLineItem> list;
			list = requisitionLineItemService.searchRequisitionLineItem(requestObj);
			return ResponseEntity.status(HttpStatus.OK).body(list);
		} catch (NegativeIdException e) {
			logger.error("Search requisition line item failed. NegativeIdException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.NEGATIVE_ID_NOT_ALLOWED.value()).body(null);
		} catch (Exception e) {
			logger.error("Search requisition line item failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}
	}

//	@ApiOperation(value = "Delete a requisition line item")
//	@DeleteMapping("/requisitionLineItem/{id}")
//	public ResponseEntity<Void> deleteRequisitionLineItem(@PathVariable Long id) {
//		try {
//			requisitionLineItemService.deleteRequisitionLineItem(id);
//			return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert("requisitionLineItem", false,
//					"requisitionLineItem", id.toString())).build();
//		} catch (NegativeIdException e) {
//			logger.error("Delete requisition line item failed. NegativeIdException: ", e.getMessage());
//			return ResponseEntity.status(BusinessValidationCodes.NEGATIVE_ID_NOT_ALLOWED.value()).body(null);
//		} catch (IdNotFoundException e) {
//			logger.error("Delete requisition line item failed. IdNotFoundException: ", e.getMessage());
//			return ResponseEntity.status(BusinessValidationCodes.ID_NOT_FOUND.value()).body(null);
//		} catch (DataNotFoundException e) {
//			logger.error("Delete requisition line item failed. DataNotFoundException: ", e.getMessage());
//			return ResponseEntity.status(BusinessValidationCodes.DATA_NOT_FOUND.value()).body(null);
//		} catch (Exception e) {
//			logger.error("Delete requisition line item failed. Exception: ", e);
//			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
//		}
//
//	}
	@ApiOperation(value = "Search a requisition line item by id")
	@GetMapping("/requisitionLineItem/{id}")
	public ResponseEntity<RequisitionLineItem> getRequisitionLineItem(@PathVariable Long id) {
		logger.info("Getting requisitionLineItem by id: " + id);
		Map<String, String> venObj = new HashMap<>();
		try {
			RequisitionLineItem requisitionLineItem = null;
			venObj.put("id", String.valueOf(id));
			List<RequisitionLineItem> reuilineList;

			reuilineList = requisitionLineItemService.searchRequisitionLineItem(venObj);
			if (reuilineList.size() > 0) {
				requisitionLineItem = reuilineList.get(0);
			}
			return ResponseEntity.status(HttpStatus.OK).body(requisitionLineItem);
		} catch (NegativeIdException e) {
			logger.error("Search requisition line item failed. NegativeIdException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.NEGATIVE_ID_NOT_ALLOWED.value()).body(null);
		} catch (Exception e) {
			logger.error("Search requisition line item failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}

	}
	
	@DeleteMapping("/requisitionLineItem/{id}")
	public ResponseEntity<Boolean> deleteRequisitionLineItem(@PathVariable Long id) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode obj = mapper.createObjectNode();
		obj.put("id", id);
		obj.put("status", Constants.STATUS_DEACTIVE);

		try {
			RequisitionLineItem req = requisitionLineItemService.updateRequisitionLineItem(obj.toString(),null);
			if (Constants.STATUS_DEACTIVE.equalsIgnoreCase(req.getStatus())) {
				return ResponseEntity.status(HttpStatus.OK).body(Boolean.TRUE);
			} else {
				return ResponseEntity.status(BusinessValidationCodes.DELETION_FAILED.value()).body(Boolean.FALSE);
			}
		} catch (IOException e) {
			logger.error("Delete requisition failed. IOException: ", e);
			return ResponseEntity.status(BusinessValidationCodes.IO_EXCEPTION.value()).body(null);
		} catch (NegativeIdException e) {
			logger.error("Delete requisition failed. NegativeIdException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.NEGATIVE_ID_NOT_ALLOWED.value()).body(null);
		} catch (IdNotFoundException e) {
			logger.error("Delete requisition failed. IdNotFoundException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.ID_NOT_FOUND.value()).body(null);
		} catch (DataNotFoundException e) {
			logger.error("Delete requisition failed. DataNotFoundException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.DATA_NOT_FOUND.value()).body(null);
		} catch (Exception e) {
			logger.error("Delete requisition failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}
	}

}
