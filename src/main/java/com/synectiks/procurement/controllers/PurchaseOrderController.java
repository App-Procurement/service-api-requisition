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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.synectiks.procurement.business.service.PurchaseOrderService;
import com.synectiks.procurement.domain.PurchaseOrder;

import io.github.jhipster.web.util.HeaderUtil;

@RestController
@RequestMapping("/api")
public class PurchaseOrderController {
	private static final Logger logger = LoggerFactory.getLogger(PurchaseOrderController.class);

	@Autowired
	private PurchaseOrderService purchaseOrderService;
	
	@PostMapping("/purchaseOrder")
	public ResponseEntity<PurchaseOrder> addPurchaseOrder(@RequestBody ObjectNode obj){
		logger.info("Request to add purchase order");
			PurchaseOrder purchaseOrder = purchaseOrderService.addPurchaseOrder(obj);
			return ResponseEntity.status(HttpStatus.OK).body(purchaseOrder);
	}
	@PutMapping("/purchaseOrder")
	public ResponseEntity<PurchaseOrder> updatePurchaseOrder(@RequestBody ObjectNode obj){
		logger.info("Request to update purchase order");
			PurchaseOrder purchaseOrder = purchaseOrderService.updatePurchaseOrder(obj);
			return ResponseEntity.status(HttpStatus.OK).body(purchaseOrder);
	}

	@GetMapping("/purchaseOrder")
	public ResponseEntity<List<PurchaseOrder>> searchPurchaseOrder(@RequestParam Map<String, String> requestObj) {
		logger.info("Request to get purchase orders on given filter criteria");
			List<PurchaseOrder> list = purchaseOrderService.searchPurchaseOrder(requestObj);
			return ResponseEntity.status(HttpStatus.OK).body(list);
	}

	@DeleteMapping("/purchaseOrder/{id}")
	public ResponseEntity<Void> deletePurchaseOrder(@PathVariable Long id) {
			purchaseOrderService.deletePurchaseOrder(id);
			return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert("purchaseOrder", false, "purchaseOrder", id.toString())).build();
	}

	@GetMapping("/purchaseOrder/{id}")
	public ResponseEntity<PurchaseOrder> getPurchaseOrder(@PathVariable Long id) {
		logger.info("Getting invoice by id: " + id);
			PurchaseOrder purchaseOrder = purchaseOrderService.getPurchaseOrder(id);
			return ResponseEntity.status(HttpStatus.OK).body(purchaseOrder);
	}
	
	@PostMapping("/purchaseOrder/approve")
	public ResponseEntity<Boolean> approvePurchaseOrder(@RequestBody ObjectNode obj) {
		logger.info("Request to approve a purchase order");
		boolean updateFlag = purchaseOrderService.approvePurchaseOrder(obj);
			return ResponseEntity.status(HttpStatus.OK).body(updateFlag);
	}
}
