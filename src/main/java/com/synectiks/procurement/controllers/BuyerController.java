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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.synectiks.procurement.business.service.BuyerService;
import com.synectiks.procurement.config.Constants;
import com.synectiks.procurement.domain.Buyer;

import io.github.jhipster.web.util.HeaderUtil;

@RestController
@RequestMapping("/api")
public class BuyerController {
	private static final Logger logger = LoggerFactory.getLogger(BuyerController.class);

	@Autowired
	BuyerService buyerService;

	@PostMapping("/buyer")
	public ResponseEntity<Buyer> addBuyer(@RequestBody ObjectNode obj)  {
		logger.info("Request to add a buyer");
		Buyer  buyer = buyerService.addBuyer(obj);
		return ResponseEntity.status(HttpStatus.OK).body(buyer);
	}

	@PutMapping("/buyer")
	public ResponseEntity<Buyer> updateBuyer(@RequestBody ObjectNode obj){
		logger.info("Request to update a buyer");
//		Buyer buyer=null;
			Buyer buyer = buyerService.updateBuyer(obj);
			return ResponseEntity.status(HttpStatus.OK).body(buyer);
	}

	@GetMapping("/buyer")
	public ResponseEntity<List<Buyer>> searchBuyer(@RequestParam Map<String, String> requestObj) {
		logger.info("Request to search buyer on given filter criteria");
			List<Buyer> list = buyerService.searchBuyer(requestObj);
			return ResponseEntity.status(HttpStatus.OK).body(list);
	}

	@DeleteMapping("/buyer/{id}")
	public ResponseEntity<Void> deleteBuyer(@PathVariable Long id) {
		logger.info("Request to delete a buyer");
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode obj = mapper.createObjectNode();
		obj.put("id", id);
		obj.put("status", Constants.STATUS_DEACTIVE);
		buyerService.updateBuyer(obj);
			return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert("buyer", false, "buyer", id.toString())).build();
	}

	@GetMapping("/buyer/{id}")
	public ResponseEntity<Buyer> getBuyer(@PathVariable Long id) {
		logger.info("Getting buyer by id: " + id);
			Buyer buyer = buyerService.getBuyer(id);
			return ResponseEntity.status(HttpStatus.OK).body(buyer);
 }
}

//....testing.......
