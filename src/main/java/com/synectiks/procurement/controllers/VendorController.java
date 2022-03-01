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
import com.synectiks.procurement.business.service.VendorService;
import com.synectiks.procurement.domain.Vendor;

import io.github.jhipster.web.util.HeaderUtil;

@RestController
@RequestMapping("/api")
public class VendorController {
	private static final Logger logger = LoggerFactory.getLogger(VendorController.class);

	@Autowired
	VendorService vendorService;

	@PostMapping("/vendor")
	public ResponseEntity<Vendor> addVendor(@RequestBody ObjectNode obj) {
		logger.info("Request to add a vendor");
		Vendor vendor = vendorService.addVendor(obj);
		return ResponseEntity.status(HttpStatus.OK).body(vendor);
	}

	@PutMapping("/vendor")
	public ResponseEntity<Vendor> updateVendor(@RequestBody ObjectNode obj) {
		logger.info("Request to update a vendor");
		Vendor vendor = vendorService.updateVendor(obj);
		return ResponseEntity.status(HttpStatus.OK).body(vendor);
	}

	@GetMapping("/vendor")
	public ResponseEntity<List<Vendor>> searchVendor(@RequestParam Map<String, String> requestObj) {
		logger.info("Request to search vendor on given filter criteria");
		List<Vendor> list = vendorService.searchVendor(requestObj);
		return ResponseEntity.status(HttpStatus.OK).body(list);
	}

	@DeleteMapping("/vendor/{id}")
	public ResponseEntity<Void> deleteVendor(@PathVariable Long id) {
		logger.info("Request to delete a vendor");
		vendorService.deleteVender(id);
		return ResponseEntity.noContent()
				.headers(HeaderUtil.createEntityDeletionAlert("vendor", false, "vendor", id.toString())).build();
	}

	@GetMapping("/vendor/{id}")
	public ResponseEntity<Vendor> getVendor(@PathVariable Long id) {
		logger.info("Getting vendor by id: " + id);
		Vendor vendor = vendorService.getVendor(id);
		return ResponseEntity.status(HttpStatus.OK).body(vendor);
	}
}
