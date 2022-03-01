package com.synectiks.procurement.controllers;

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
import com.synectiks.procurement.business.service.RulesService;
import com.synectiks.procurement.domain.Rules;
import com.synectiks.procurement.web.rest.errors.UniqueConstraintException;

import io.github.jhipster.web.util.HeaderUtil;

@RestController
@RequestMapping("/api")
public class RulesController {
	private static final Logger logger = LoggerFactory.getLogger(RulesController.class);

	@Autowired
	RulesService rulesService;

	@PostMapping("/rules")
	public ResponseEntity<Rules> addRules(@RequestBody ObjectNode obj) throws UniqueConstraintException {
		logger.info("Request to add a rule");
		Rules rules;
		try {
			rules = rulesService.addRules(obj);
			return ResponseEntity.status(HttpStatus.OK).body(rules);
		} catch (UniqueConstraintException e) {
			logger.error("Add rules failed. UniqueConstraintException: ", e);
			throw e;
		}
	}

	@PutMapping("/rules")
	public ResponseEntity<Rules> updateRules(@RequestBody ObjectNode obj)
			throws UniqueConstraintException, JSONException {
		logger.info("Request to update a rule");
		Rules rules;
		try {
			rules = rulesService.updateRules(obj);
			return ResponseEntity.status(HttpStatus.OK).body(rules);
		} catch (UniqueConstraintException e) {
			logger.error("Update rules failed. UniqueConstraintException: ", e);
			throw e;
		} catch (JSONException e) {
			logger.error("Update rules failed. JSONException: ", e);
			throw e;
		}
	}

	@GetMapping("/rules")
	public ResponseEntity<List<Rules>> searchRules(@RequestParam Map<String, String> requestObj) {
		logger.info("Request to search rule on given filter criteria");
		List<Rules> list = rulesService.searchRules(requestObj);
		return ResponseEntity.status(HttpStatus.OK).body(list);
	}

	@DeleteMapping("/rules/{id}")
	public ResponseEntity<Void> deleteRules(@PathVariable Long id) {
		logger.info("Request to delete a rules");
		rulesService.deleteRules(id);
		return ResponseEntity.noContent()
				.headers(HeaderUtil.createEntityDeletionAlert("rules", false, "rules", id.toString())).build();
	}

	@GetMapping("/rules/{id}")
	public ResponseEntity<Rules> getRules(@PathVariable Long id) {
		logger.info("Getting rule by id: " + id);
		Rules rules = rulesService.getRules(id);
		return ResponseEntity.status(HttpStatus.OK).body(rules);
	}

	@GetMapping("/rules/{name}")
	public ResponseEntity<Rules> getRulesByName(@PathVariable String name) {
		logger.info("Getting rule by name: " + name);
		Rules rules = rulesService.getRulesByName(name);
		return ResponseEntity.status(HttpStatus.OK).body(rules);
	}
}