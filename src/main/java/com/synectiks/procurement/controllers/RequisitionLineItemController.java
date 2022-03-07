package com.synectiks.procurement.controllers;

import java.io.IOException;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.synectiks.procurement.business.service.RequisitionLineItemService;
import com.synectiks.procurement.domain.RequisitionLineItem;

import io.github.jhipster.web.util.HeaderUtil;

@RestController
@RequestMapping("/api")
public class RequisitionLineItemController {
	private static final Logger logger = LoggerFactory.getLogger(RequisitionLineItemController.class);

	@Autowired
	private RequisitionLineItemService requisitionLineItemService;

	@PostMapping("/requisitionLineItem")
	public ResponseEntity<RequisitionLineItem> addRequisitionLineItem(
			@RequestParam(name = "requisitionLineItemFile", required = false) MultipartFile[] requisitionLineItemFile,
			@RequestParam("obj") String obj) throws IOException {
		logger.info("Request to add a requisition line item");
		RequisitionLineItem requisitionLineItem;
		try {
			requisitionLineItem = requisitionLineItemService.addRequisitionLineItem(obj, requisitionLineItemFile);
			return ResponseEntity.status(HttpStatus.OK).body(requisitionLineItem);
		} catch (IOException e) {
			logger.error("Add requisition line item failed. IOException: ", e);
			throw e;
		}
	}

	@PutMapping("/requisitionLineItem")
	public ResponseEntity<RequisitionLineItem> updateRequisitionLineItem(
			@RequestParam(name = "requisitionLineItemFile", required = false) MultipartFile[] requisitionLineItemFile,
			@RequestParam("obj") String obj) throws IOException {
		logger.info("Request to update a requsition");
		try {
			RequisitionLineItem requisitionLineItem = requisitionLineItemService.updateRequisitionLineItem(obj,
					requisitionLineItemFile);
			return ResponseEntity.status(HttpStatus.OK).body(requisitionLineItem);
		} catch (IOException e) {
			logger.error("Add requisition line item failed. IOException: ", e);
			throw e;
		}
	}

	@GetMapping("/requisitionLineItem")
	public ResponseEntity<List<RequisitionLineItem>> searchRequisitionLineItem(
			@RequestParam Map<String, String> requestObj) {
		logger.info("Request to get requisition line item on given filter criteria");
		List<RequisitionLineItem> list = requisitionLineItemService.searchRequisitionLineItem(requestObj);
		return ResponseEntity.status(HttpStatus.OK).body(list);
	}

	@DeleteMapping("/requisitionLineItem/{id}")
	public ResponseEntity<Void> deleteRequisitionLineItem(@PathVariable Long id) {
		requisitionLineItemService.deleteRequisitionLineItem(id);
		return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert("requisitionLineItem", false,
				"requisitionLineItem", id.toString())).build();
	}

	@GetMapping("/requisitionLineItem/{id}")
	public ResponseEntity<RequisitionLineItem> getRequisitionLineItem(@PathVariable Long id) {
		logger.info("Getting requisitionLineItem by id: " + id);
		RequisitionLineItem requisitionLineItem = requisitionLineItemService.getRequisitionLineItem(id);
		return ResponseEntity.status(HttpStatus.OK).body(requisitionLineItem);
	}

}