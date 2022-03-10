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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.synectiks.procurement.business.service.PurchaseOrderService;
import com.synectiks.procurement.config.BusinessValidationCodes;
import com.synectiks.procurement.config.Constants;
import com.synectiks.procurement.domain.PurchaseOrder;
import com.synectiks.procurement.web.rest.errors.DataNotFoundException;
import com.synectiks.procurement.web.rest.errors.IdNotFoundException;
import com.synectiks.procurement.web.rest.errors.NegativeIdException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "Purchase order APIs")
@RestController
@RequestMapping("/api")
public class PurchaseOrderController {
	private static final Logger logger = LoggerFactory.getLogger(PurchaseOrderController.class);

	@Autowired
	private PurchaseOrderService purchaseOrderService;

	@ApiOperation(value = "Create a new Purchase order")
	@PostMapping("/purchaseOrder")
	public ResponseEntity<PurchaseOrder> addPurchaseOrder(@RequestBody ObjectNode obj) {
		logger.info("Request to add purchase order");

		try {
			PurchaseOrder purchaseOrder = purchaseOrderService.addPurchaseOrder(obj);
			return ResponseEntity.status(HttpStatus.OK).body(purchaseOrder);
		} catch (Exception e) {
			logger.error("Add purchase order failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}

	}

	@ApiOperation(value = "Update an existing Purchase order")
	@PutMapping("/purchaseOrder")
	public ResponseEntity<PurchaseOrder> updatePurchaseOrder(@RequestBody ObjectNode obj) {
		logger.info("Request to update purchase order");

		try {
			PurchaseOrder purchaseOrder = purchaseOrderService.updatePurchaseOrder(obj);
			return ResponseEntity.status(HttpStatus.OK).body(purchaseOrder);
		} catch (NegativeIdException e) {
			logger.error("Update purchase order failed. NegativeIdException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.NEGATIVE_ID_NOT_ALLOWED.value()).body(null);
		} catch (IdNotFoundException e) {
			logger.error("Update purchase order failed. IdNotFoundException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.ID_NOT_FOUND.value()).body(null);
		} catch (DataNotFoundException e) {
			logger.error("Update purchase order failed. DataNotFoundException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.DATA_NOT_FOUND.value()).body(null);
		} catch (Exception e) {
			logger.error("Update purchase order failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}
	}

	@ApiOperation(value = "Search Purchase order")
	@GetMapping("/purchaseOrder")
	public ResponseEntity<List<PurchaseOrder>> searchPurchaseOrder(@RequestParam Map<String, String> requestObj) {
		logger.info("Request to get purchase orders on given filter criteria");

		try {
			List<PurchaseOrder> list = purchaseOrderService.searchPurchaseOrder(requestObj);
			return ResponseEntity.status(HttpStatus.OK).body(list);
		} catch (NegativeIdException e) {
			logger.error("Search purchase orders failed. NegativeIdException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.NEGATIVE_ID_NOT_ALLOWED.value()).body(null);
		} catch (Exception e) {
			logger.error("Search purchase orders failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}
	}
	
	
	
	
	@ApiOperation(value = "Delete a purchaseOrder")
	@DeleteMapping("/purchaseOrder/{id}")
	public ResponseEntity<Boolean> deletepurchaseOrder(@PathVariable Long id) {
		logger.info("Request to delete a purchaseOrder");
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode obj = mapper.createObjectNode();
		obj.put("id", id);
		obj.put("status", Constants.STATUS_DEACTIVE);
		try {
		PurchaseOrder bu=purchaseOrderService.updatePurchaseOrder(obj);
			if(Constants.STATUS_DEACTIVE.equalsIgnoreCase(bu.getStatus())){
				return ResponseEntity.status(HttpStatus.OK).body(Boolean.TRUE);
			}else {
				return ResponseEntity.status(BusinessValidationCodes.DELETION_FAILED.value()).body(Boolean.FALSE);
			}
		} 
		catch (NegativeIdException e) {
			logger.error("Delete purchaseOrder failed. NegativeIdException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.NEGATIVE_ID_NOT_ALLOWED.value()).body(null);
		} catch (IdNotFoundException e) {
			logger.error("Delete purchaseOrder failed. IdNotFoundException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.ID_NOT_FOUND.value()).body(null);
		} catch (DataNotFoundException e) {
			logger.error("Delete purchaseOrder failed. DataNotFoundException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.DATA_NOT_FOUND.value()).body(null);
		}
		catch (Exception e) {
			logger.error("Delete purchaseOrder failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}
			
	}
	

//	@ApiOperation(value = "Delete a Purchase order")
//	@DeleteMapping("/purchaseOrder/{id}")
//	public ResponseEntity<Boolean> deletePurchaseOrder(@PathVariable Long id) {
//
//		try {
//			boolean delpurch = purchaseOrderService.deletePurchaseOrder(id);
//			if (delpurch) {
//				return ResponseEntity.status(HttpStatus.OK).body(delpurch);
//			} else {
//				return ResponseEntity.status(HttpStatus.OK).body(!delpurch);
//			}
//		} catch (NegativeIdException e) {
//			logger.error("Delete purchase orders failed. NegativeIdException: ", e.getMessage());
//			return ResponseEntity.status(BusinessValidationCodes.NEGATIVE_ID_NOT_ALLOWED.value()).body(null);
//		} catch (IdNotFoundException e) {
//			logger.error("Delete purchase orders failed. IdNotFoundException: ", e.getMessage());
//			return ResponseEntity.status(BusinessValidationCodes.ID_NOT_FOUND.value()).body(null);
//		} catch (DataNotFoundException e) {
//			logger.error("Delete purchase orders failed. DataNotFoundException: ", e.getMessage());
//			return ResponseEntity.status(BusinessValidationCodes.DATA_NOT_FOUND.value()).body(null);
//		} catch (Exception e) {
//			logger.error("Delete purchase orders failed. Exception: ", e);
//			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
//		}
//	}

	@ApiOperation(value = "Search a purchase order by id")
	@GetMapping("/purchaseOrder/{id}")
	public ResponseEntity<PurchaseOrder> getPurchaseOrder(@PathVariable Long id) {
		logger.info("Getting invoice by id: " + id);

		try {
			Map<String, String> purObj = new HashMap<>();

			PurchaseOrder purchaseOrder = null;
			purObj.put("id", String.valueOf(id));
			List<PurchaseOrder> purList;

			purList = purchaseOrderService.searchPurchaseOrder(purObj);
			if (purList.size() > 0) {
				purchaseOrder = purList.get(0);
			}
			return ResponseEntity.status(HttpStatus.OK).body(purchaseOrder);
		} catch (NegativeIdException e) {
			logger.error("Search purchase orders failed. NegativeIdException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.NEGATIVE_ID_NOT_ALLOWED.value()).body(null);
		} catch (Exception e) {
			logger.error("Search purchase orders failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}
	}

	@ApiOperation(value = "Approve a purchase order")
	@PostMapping("/purchaseOrder/approve")
	public ResponseEntity<Boolean> approvePurchaseOrder(@RequestBody ObjectNode obj) {
		logger.info("Request to approve a purchase order");
		boolean updateFlag;
		try {
			updateFlag = purchaseOrderService.approvePurchaseOrder(obj);
			return ResponseEntity.status(HttpStatus.OK).body(updateFlag);
		} catch (IdNotFoundException e) {
			logger.error("Approve purchase order failed. IdNotFoundException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.ID_NOT_FOUND.value()).body(null);
		} catch (NegativeIdException e) {
			logger.error("Approve purchase order failed. NegativeIdException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.NEGATIVE_ID_NOT_ALLOWED.value()).body(null);
		} catch (JSONException e) {
			logger.error("Approve purchase order failed. JSONException: ", e);
			return ResponseEntity.status(BusinessValidationCodes.JSON_EXCEPTION.value()).body(null);
		} catch (Exception e) {
			logger.error("Approve purchase order failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}
	}

}
