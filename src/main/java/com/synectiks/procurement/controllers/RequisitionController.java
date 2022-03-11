package com.synectiks.procurement.controllers;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.synectiks.procurement.business.service.RequisitionService;
import com.synectiks.procurement.config.BusinessValidationCodes;
import com.synectiks.procurement.config.Constants;
import com.synectiks.procurement.domain.Requisition;
import com.synectiks.procurement.domain.VendorRequisitionBucket;
import com.synectiks.procurement.web.rest.errors.DataNotFoundException;
import com.synectiks.procurement.web.rest.errors.IdNotFoundException;
import com.synectiks.procurement.web.rest.errors.NegativeIdException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "Requisition APIs")
@RestController
@RequestMapping("/api")
public class RequisitionController {
	private static final Logger logger = LoggerFactory.getLogger(RequisitionController.class);

	@Autowired
	private RequisitionService requisitionService;

	@ApiOperation(value = "Create a new requisition")
	@RequestMapping(value = "/requisitions", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Requisition> addRequisition(
			@RequestParam("obj") String obj,
			@RequestParam(name = "requisitionFile", required = false) MultipartFile[] requisitionFile,
			@RequestParam(name = "requisitionLineItemFile", required = false) MultipartFile[] requisitionLineItemFile)
			 {
		logger.info("Request to add a requsition");
		try {
			Requisition requisition = requisitionService.addRequisition(requisitionFile, requisitionLineItemFile, obj);
			return ResponseEntity.status(HttpStatus.OK).body(requisition);
		} catch (JSONException e) {
			logger.error("Add requisition failed. JSONException: ", e);
			return ResponseEntity.status(BusinessValidationCodes.JSON_EXCEPTION.value()).body(null);
		} catch (IOException e) {
			logger.error("Add requisition failed. IOException: ", e);
			return ResponseEntity.status(BusinessValidationCodes.IO_EXCEPTION.value()).body(null);
		} catch (NegativeIdException e) {
			logger.error("Add requisition failed. NegativeIdException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.NEGATIVE_ID_NOT_ALLOWED.value()).body(null);
		} catch (Exception e) {
			logger.error("Update requisition failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}

	}

	@ApiOperation(value = "Update an existing requisition")
	@RequestMapping(value = "/requisitions", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Requisition> updateRequisition(
			@RequestParam(name = "requisitionFile", required = false) MultipartFile[] requisitionFile,
			@RequestParam(name = "requisitionLineItemFile", required = false) MultipartFile[] requisitionLineItemFile,
			@RequestParam("obj") String obj) {
		logger.info("Request to update a requsition");
		try {
//			String obj = objNode.toPrettyString();
			Requisition requisition = requisitionService.updateRequisition(requisitionFile, requisitionLineItemFile,
					obj);
			return ResponseEntity.status(HttpStatus.OK).body(requisition);
		} catch (JSONException e) {
			logger.error("Update requisition failed. JSONException: ", e);
			return ResponseEntity.status(BusinessValidationCodes.JSON_EXCEPTION.value()).body(null);
		} catch (IOException e) {
			logger.error("Update requisition failed. IOException: ", e);
			return ResponseEntity.status(BusinessValidationCodes.IO_EXCEPTION.value()).body(null);
		} catch (NegativeIdException e) {
			logger.error("Update requisition failed. NegativeIdException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.NEGATIVE_ID_NOT_ALLOWED.value()).body(null);
		} catch (IdNotFoundException e) {
			logger.error("Update requisition failed. IdNotFoundException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.ID_NOT_FOUND.value()).body(null);
		} catch (DataNotFoundException e) {
			logger.error("Update requisition failed. DataNotFoundException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.DATA_NOT_FOUND.value()).body(null);
		} catch (Exception e) {
			logger.error("Update requisition failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}
	}

	@ApiOperation(value = "Search requisitions")
	@GetMapping("/requisitions")
	public ResponseEntity<List<Requisition>> searchRequisition(@RequestParam Map<String, String> requestObj)
			throws ParseException {
		logger.info("Request to search requsitions");
		try {
			List<Requisition> list = requisitionService.searchRequisition(requestObj);
			return ResponseEntity.status(HttpStatus.OK).body(list);
		} catch (ParseException e) {
			logger.error("Search requisition failed. ParseException: ", e);
			return ResponseEntity.status(BusinessValidationCodes.PARSE_EXCEPTION.value()).body(null);
		} catch (NegativeIdException e) {
			logger.error("Search requisition failed. NegativeIdException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.NEGATIVE_ID_NOT_ALLOWED.value()).body(null);
		} catch (Exception e) {
			logger.error("Search requisition failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}

	}

	@ApiOperation(value = "Delete a requisition")
	@DeleteMapping("/requisitions/{id}")
	public ResponseEntity<Boolean> deleteRequisition(@PathVariable Long id) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode obj = mapper.createObjectNode();
		obj.put("id", id);
		obj.put("status", Constants.STATUS_DEACTIVE);

		try {
			Requisition req = requisitionService.updateRequisition(null, null, obj.toString());
			if (Constants.STATUS_DEACTIVE.equalsIgnoreCase(req.getStatus())) {
				return ResponseEntity.status(HttpStatus.OK).body(Boolean.TRUE);
			} else {
				return ResponseEntity.status(BusinessValidationCodes.DELETION_FAILED.value()).body(Boolean.FALSE);
			}
		} catch (JSONException e) {
			logger.error("Delete requisition failed. JSONException: ", e);
			return ResponseEntity.status(BusinessValidationCodes.JSON_EXCEPTION.value()).body(null);
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

	@ApiOperation(value = "Search a requisition by id")
	@GetMapping("/requisitions/{id}")
	public ResponseEntity<Requisition> getRequisitionById(@PathVariable Long id) {
		logger.info("Getting requisition by id: " + id);
		Map<String, String> reqObj = new HashMap<>();
		try {
			Requisition requisition = null;
			reqObj.put("id", String.valueOf(id));
			List<Requisition> reqList = requisitionService.searchRequisition(reqObj);
			if (reqList.size() > 0) {
				requisition = reqList.get(0);
			}
			return ResponseEntity.status(HttpStatus.OK).body(requisition);
		} catch (ParseException e) {
			logger.error("Getting requisition by id failed. ParseException: ", e);
			return ResponseEntity.status(BusinessValidationCodes.PARSE_EXCEPTION.value()).body(null);
		} catch (NegativeIdException e) {
			logger.error("Getting requisition by id failed. NegativeIdException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.NEGATIVE_ID_NOT_ALLOWED.value()).body(null);
		} catch (Exception e) {
			logger.error("Getting requisition by id failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}
	}

	@ApiOperation(value = "Assign selected requisitions to selected vendors")
	@PostMapping("/requisitions/sendToVendor")
	public ResponseEntity<List<VendorRequisitionBucket>> sendRequisitionToVendor(@RequestBody List<ObjectNode> list) {
		logger.info("Assigning requisitions to vendors ");
		try {
			List<VendorRequisitionBucket> vReqList = requisitionService.sendRequisitionToVendor(list);
			return ResponseEntity.status(HttpStatus.OK).body(vReqList);
		} catch (NegativeIdException e) {
			logger.error("Send to vendor failed. NegativeIdException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.NEGATIVE_ID_NOT_ALLOWED.value()).body(null);
		} catch (IdNotFoundException e) {
			logger.error("Send to vendor failed. IdNotFoundException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.ID_NOT_FOUND.value()).body(null);
		} catch (DataNotFoundException e) {
			logger.error("Send to vendor failed. DataNotFoundException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.DATA_NOT_FOUND.value()).body(null);
		} catch (Exception e) {
			logger.error("Send to vendor failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}
	}

	@ApiOperation(value = "Approve a requisition")
	@PostMapping("/requisitions/approve")
	public ResponseEntity<Boolean> approveRequisition(@RequestBody ObjectNode obj) {
		logger.info("Request to approve a requsition");
		try {
			Boolean updateFlag = requisitionService.approveRequisition(obj);
			return ResponseEntity.status(HttpStatus.OK).body(updateFlag);
		} catch (IdNotFoundException e) {
			logger.error("Approve requisition failed. IdNotFoundException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.ID_NOT_FOUND.value()).body(null);
		} catch (NegativeIdException e) {
			logger.error("Approve requisition failed. NegativeIdException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.NEGATIVE_ID_NOT_ALLOWED.value()).body(null);
		} catch (Exception e) {
			logger.error("Approve requisition failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}

	}

}