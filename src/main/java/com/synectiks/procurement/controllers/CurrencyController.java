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
import com.synectiks.procurement.business.service.CurrencyService;
import com.synectiks.procurement.domain.Currency;

import io.github.jhipster.web.util.HeaderUtil;

@RestController
@RequestMapping("/api")
//@Api(value = "/api", tags = "Currency Management")
public class CurrencyController {
	private static final Logger logger = LoggerFactory.getLogger(CurrencyController.class);

	@Autowired
	private CurrencyService currencyService;

	@PostMapping("/currency")
	public ResponseEntity<Currency> addCurrency(@RequestBody ObjectNode obj){
		logger.info("Request to add currency");
			Currency currency = currencyService.addCurrency(obj);
			return ResponseEntity.status(HttpStatus.OK).body(currency);
	}

	@PutMapping("/currency")
	public ResponseEntity<Currency> updateCurrency(@RequestBody ObjectNode obj){
		logger.info("Request to updating currency");
			Currency currency = currencyService.updateCurrency(obj);
			return ResponseEntity.status(HttpStatus.OK).body(currency);
	}

	@GetMapping("/currency")
	public ResponseEntity<List<Currency>> searchCurrency(@RequestParam Map<String, String> requestObj) {
		logger.info("Request to get currency on given filter criteria");
			List<Currency> list = currencyService.searchCurrency(requestObj);
			return ResponseEntity.status(HttpStatus.OK).body(list);
	}

	@DeleteMapping("/currency/{id}")
	public ResponseEntity<Void> deleteCurrency(@PathVariable Long id) {
			currencyService.deleteCurrency(id);
			return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert("currency", false, "currency", id.toString())).build();
	}

	@GetMapping("/currency/{id}")
	public ResponseEntity<Currency> getCurrency(@PathVariable Long id) {
		logger.info("Getting currency by id: " + id);
			Currency currency = currencyService.getCurrency(id);
			return ResponseEntity.status(HttpStatus.OK).body(currency);
	}
}
