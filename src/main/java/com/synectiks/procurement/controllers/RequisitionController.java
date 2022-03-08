package com.synectiks.procurement.controllers;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.synectiks.procurement.business.service.XformAwsS3Config;
import com.synectiks.procurement.business.service.RequisitionService;
import com.synectiks.procurement.config.Constants;
import com.synectiks.procurement.domain.Requisition;
import com.synectiks.procurement.domain.VendorRequisitionBucket;

import io.github.jhipster.web.util.HeaderUtil;

@RestController
@RequestMapping("/api")
public class RequisitionController {
	private static final Logger logger = LoggerFactory.getLogger(RequisitionController.class);

	@Autowired
	private RequisitionService requisitionService;

	@RequestMapping(value = "/requisitions", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Requisition> addRequisition   (
			@RequestParam(name = "requisitionFile", required = false) MultipartFile[] requisitionFile,
			@RequestParam(name = "requisitionLineItemFile", required = false) MultipartFile[] requisitionLineItemFile,
			@RequestBody ObjectNode objNode) {
		logger.info("Request to add a requsition");
		try {
			String obj = objNode.toPrettyString();
			Requisition requisition = requisitionService.addRequisition(requisitionFile,requisitionLineItemFile, obj);
			return ResponseEntity.status(HttpStatus.OK).body(requisition);
		} catch (JsonMappingException e) {
			logger.error("Add requisition failed. JsonMappingException: ", e);
		} catch (JsonProcessingException e) {
			logger.error("Add requisition failed. JsonProcessingException: ", e);
		} catch (JSONException e) {
			logger.error("Add requisition failed. JSONException: ", e);
		} catch (IOException e) {
			logger.error("Add requisition failed. IOException: ", e);
		} catch (ParseException e) {
			logger.error("Add requisition failed. ParseException: ", e);
		}
		return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
	}

	@RequestMapping(value = "/requisitions", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Requisition> updateRequisition(
			@RequestParam(name = "requisitionFile", required = false) MultipartFile[] requisitionFile,
			@RequestParam(name = "requisitionLineItemFile", required = false) MultipartFile[] requisitionLineItemFile,
			@RequestParam("obj") String obj) {
		logger.info("Request to update a requsition");
		try {
			Requisition requisition = requisitionService.updateRequisition(requisitionFile,requisitionLineItemFile, obj);
			return ResponseEntity.status(HttpStatus.OK).body(requisition);
		} catch (JsonMappingException e) {
			logger.error("Add requisition failed. JsonMappingException: ", e);
		} catch (JsonProcessingException e) {
			logger.error("Add requisition failed. JsonProcessingException: ", e);
		} catch (JSONException e) {
			logger.error("Add requisition failed. JSONException: ", e);
		} catch (IOException e) {
			logger.error("Add requisition failed. IOException: ", e);
		}
		return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
	}

	@GetMapping("/requisitions")
	public ResponseEntity<List<Requisition>> searchRequisition(@RequestParam Map<String, String> requestObj) throws ParseException {
		logger.info("Request to search requsitions");
		try {
			List<Requisition> list = requisitionService.searchRequisition(requestObj);
			return ResponseEntity.status(HttpStatus.OK).body(list);
		} catch (ParseException e) {
			logger.error("Search requisition failed. ParseException: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}

	}

	@DeleteMapping("/requisitions/{id}")
	public ResponseEntity<Void> deleteRequisition(@PathVariable Long id) throws ParseException, JsonMappingException, JsonProcessingException, JSONException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode obj = mapper.createObjectNode();
		obj.put("id", id);
		obj.put("status", Constants.STATUS_DEACTIVE);
			
		requisitionService.updateRequisition(null,null, obj.toString());
		return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert("requisition", false, "requisition", id.toString())).build();
	}



	@GetMapping("/requisitions/{id}")
	public ResponseEntity<Requisition> getRequisitionById(@PathVariable Long id) throws ParseException{
		logger.info("Getting requisition by id: " + id);
		Map<String, String> reqObj = new HashMap<>();
		try {
			Requisition requisition = null;
			reqObj.put("id", String.valueOf(id));
			List<Requisition> reqList = requisitionService.searchRequisition(reqObj);
			if(reqList.size() > 0) {
				requisition = reqList.get(0);
			}
			return ResponseEntity.status(HttpStatus.OK).body(requisition);
		}
		catch (Exception e) {
			logger.error("Getting requisition by id failed. ParseException: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}
	}

	@PostMapping("/requisitions/sendToVendor")
	public ResponseEntity<List<VendorRequisitionBucket>> sendRequisitionToVendor(@RequestBody List<ObjectNode> list) {
		logger.info("Assigning requisitions to vendors ");
		try {
			List<VendorRequisitionBucket> vReqList = requisitionService.sendRequisitionToVendor(list);
			return ResponseEntity.status(HttpStatus.OK).body(vReqList);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}
	}

	@PostMapping("/requisitions/approve")
	public ResponseEntity<Boolean> approveRequisition(@RequestBody ObjectNode obj) {
		logger.info("Request to approve a requsition");
		try {
			Boolean updateFlag = requisitionService.approveRequisition(obj);
			return ResponseEntity.status(HttpStatus.OK).body(updateFlag);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}
		
	} 

}