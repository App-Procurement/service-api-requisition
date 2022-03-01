package com.synectiks.procurement.controllers;

import java.net.URISyntaxException;
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

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.synectiks.procurement.business.service.QuotationService;
import com.synectiks.procurement.domain.Quotation;

import io.github.jhipster.web.util.HeaderUtil;

@RestController
@RequestMapping("/api")
public class QuotationController {
	private static final Logger logger = LoggerFactory.getLogger(QuotationController.class);

	@Autowired
	private QuotationService quotationService;

	@GetMapping("/quotation/{id}")
	public ResponseEntity<Quotation> getQuotation(@PathVariable Long id) {
		logger.info("Getting quotation by id: " + id);
		Quotation quotation = quotationService.getQuotation(id);
		return ResponseEntity.status(HttpStatus.OK).body(quotation);
	}

	@PostMapping("/quotation")
	public ResponseEntity<Quotation> addQuotation(@RequestBody ObjectNode obj) {
		Quotation quotation = quotationService.addQuotation(obj);
		return ResponseEntity.status(HttpStatus.OK).body(quotation);
	}

	@PutMapping("/quotation")
	public ResponseEntity<Quotation> updateQuotation(@RequestBody ObjectNode obj)
			throws JSONException, URISyntaxException {
		Quotation quotation = quotationService.updateQuotation(obj);
		return ResponseEntity.status(HttpStatus.OK).body(quotation);
	}

	@GetMapping("/quotation")
	public ResponseEntity<List<Quotation>> searchQuotation(@RequestParam Map<String, String> requestObj) {
		logger.info("Request to search quotation on given filter criteria");
		List<Quotation> list = quotationService.searchQuotation(requestObj);
		return ResponseEntity.status(HttpStatus.OK).body(list);
	}

	@DeleteMapping("/quotation/{id}")
	public ResponseEntity<Void> deleteQuotation(@PathVariable Long id) {
		quotationService.deleteQuotation(id);
		return ResponseEntity.noContent()
				.headers(HeaderUtil.createEntityDeletionAlert("quotation", false, "quotation", id.toString())).build();

	}
}
