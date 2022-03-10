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
import com.synectiks.procurement.business.service.VendorService;
import com.synectiks.procurement.config.BusinessValidationCodes;
import com.synectiks.procurement.config.Constants;
import com.synectiks.procurement.domain.Vendor;
import com.synectiks.procurement.web.rest.errors.DataNotFoundException;
import com.synectiks.procurement.web.rest.errors.IdNotFoundException;
import com.synectiks.procurement.web.rest.errors.NegativeIdException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "Vendor APIs")
@RestController
@RequestMapping("/api")
public class VendorController {
	private static final Logger logger = LoggerFactory.getLogger(VendorController.class);

	@Autowired
	VendorService vendorService;

	@ApiOperation(value = "Create a new vendor")
	@PostMapping("/vendor")
	public ResponseEntity<Vendor> addVendor(@RequestBody ObjectNode obj) {
		logger.info("Request to add a vendor");
		try {
			Vendor vendor = vendorService.addVendor(obj);
			return ResponseEntity.status(HttpStatus.OK).body(vendor);
		} catch (Exception e) {
			logger.error("Add vendor failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}
	}

	@ApiOperation(value = "Update an existing vendor")
	@PutMapping("/vendor")
	public ResponseEntity<Vendor> updateVendor(@RequestBody ObjectNode obj) {
		logger.info("Request to update a vendor");
		Vendor vendor;
		try {
			vendor = vendorService.updateVendor(obj);
			return ResponseEntity.status(HttpStatus.OK).body(vendor);
		} catch (NegativeIdException e) {
			logger.error("Update vendor failed. NegativeIdException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.NEGATIVE_ID_NOT_ALLOWED.value()).body(null);
		} catch (IdNotFoundException e) {
			logger.error("Update vendor failed. IdNotFoundException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.ID_NOT_FOUND.value()).body(null);
		} catch (DataNotFoundException e) {
			logger.error("Update vendor failed. DataNotFoundException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.DATA_NOT_FOUND.value()).body(null);
		} catch (Exception e) {
			logger.error("Update vendor failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}
	}

	@ApiOperation(value = "Search vendor")
	@GetMapping("/vendor")
	public ResponseEntity<List<Vendor>> searchVendor(@RequestParam Map<String, String> requestObj) {
		logger.info("Request to search vendor on given filter criteria");
		List<Vendor> list;
		try {
			list = vendorService.searchVendor(requestObj);
			return ResponseEntity.status(HttpStatus.OK).body(list);
		} catch (NegativeIdException e) {
			logger.error("Search vendor failed. NegativeIdException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.NEGATIVE_ID_NOT_ALLOWED.value()).body(null);
		} catch (Exception e) {
			logger.error("Search vendor failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}
	}

	@ApiOperation(value = "Delete a vendor")
	@DeleteMapping("/vendor/{id}")
	public ResponseEntity<Boolean> deletevendor(@PathVariable Long id) {
		logger.info("Request to delete a vendor");
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode obj = mapper.createObjectNode();
		obj.put("id", id);
		obj.put("status", Constants.STATUS_DEACTIVE);
		try {
			Vendor bu = vendorService.updateVendor(obj);
			if (Constants.STATUS_DEACTIVE.equalsIgnoreCase(bu.getStatus())) {
				return ResponseEntity.status(HttpStatus.OK).body(Boolean.TRUE);
			} else {
				return ResponseEntity.status(BusinessValidationCodes.DELETION_FAILED.value()).body(Boolean.FALSE);
			}
		} catch (NegativeIdException e) {
			logger.error("Delete vendor failed. NegativeIdException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.NEGATIVE_ID_NOT_ALLOWED.value()).body(null);
		} catch (IdNotFoundException e) {
			logger.error("Delete vendor failed. IdNotFoundException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.ID_NOT_FOUND.value()).body(null);
		} catch (DataNotFoundException e) {
			logger.error("Delete vendor failed. DataNotFoundException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.DATA_NOT_FOUND.value()).body(null);
		} catch (Exception e) {
			logger.error("Delete vendor failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}

	}

//	@ApiOperation(value = "Delete a vendor")
//	@DeleteMapping("/vendor/{id}")
//	public ResponseEntity<Boolean> deleteVendor(@PathVariable Long id) {
//		logger.info("Request to delete a vendor");
//		try {
//			boolean delVendor = vendorService.deleteVender(id);
//			if (delVendor) {
//				return ResponseEntity.status(HttpStatus.OK).body(Boolean.TRUE);
//			} else {
//				return ResponseEntity.status(BusinessValidationCodes.DELETION_FAILED.value()).body(Boolean.FALSE);
//			}
//		} catch (NegativeIdException e) {
//			logger.error("Delete vendor failed. NegativeIdException: ", e.getMessage());
//			return ResponseEntity.status(BusinessValidationCodes.NEGATIVE_ID_NOT_ALLOWED.value()).body(null);
//		} catch (IdNotFoundException e) {
//			logger.error("Delete vendor failed. IdNotFoundException: ", e.getMessage());
//			return ResponseEntity.status(BusinessValidationCodes.ID_NOT_FOUND.value()).body(null);
//		} catch (DataNotFoundException e) {
//			logger.error("Delete vendor failed. DataNotFoundException: ", e.getMessage());
//			return ResponseEntity.status(BusinessValidationCodes.DATA_NOT_FOUND.value()).body(null);
//		} catch (Exception e) {
//			logger.error("Delete vendor failed. Exception: ", e);
//			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
//		}
//	}

	@ApiOperation(value = "Search a vendor by id")
	@GetMapping("/vendor/{id}")
	public ResponseEntity<Vendor> getVendor(@PathVariable Long id) {
		logger.info("Getting vendor by id: " + id);
		Map<String, String> venObj = new HashMap<>();
		try {
			Vendor vendor = null;
			venObj.put("id", String.valueOf(id));
			List<Vendor> venList;

			venList = vendorService.searchVendor(venObj);
			if (venList.size() > 0) {
				vendor = venList.get(0);
			}
			return ResponseEntity.status(HttpStatus.OK).body(vendor);
		} catch (NegativeIdException e) {
			logger.error("Search vendor failed. NegativeIdException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.NEGATIVE_ID_NOT_ALLOWED.value()).body(null);
		} catch (Exception e) {
			logger.error("Search vendor failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}

	}
}
