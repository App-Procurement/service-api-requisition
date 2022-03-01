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
import com.synectiks.procurement.business.service.CommitteeService;
import com.synectiks.procurement.config.Constants;
import com.synectiks.procurement.domain.Committee;
import io.github.jhipster.web.util.HeaderUtil;

@RestController
@RequestMapping("/api")
public class CommitteeController {
	private static final Logger logger = LoggerFactory.getLogger(CommitteeController.class);

	@Autowired
	private CommitteeService committeeService;

	@PostMapping("/committee")
	public ResponseEntity<Committee> addCommittee(@RequestBody ObjectNode obj) {
		logger.info("Request to add New committee");
		Committee committee = committeeService.addCommittee(obj);
		return ResponseEntity.status(HttpStatus.OK).body(committee);
	}

	@PutMapping("/committee")
	public ResponseEntity<Committee> updateCommittee(@RequestBody ObjectNode obj){
		logger.info("Request to update a committee");
			Committee committee = committeeService.updateCommittee(obj);
			return ResponseEntity.status(HttpStatus.OK).body(committee);

	}

	@GetMapping("/committee")
	public ResponseEntity<List<Committee>> searchCommittee(@RequestParam Map<String, String> requestObj) {
		logger.info("Request to get list of committee on given filter criteria");
		List<Committee> list = committeeService.searchCommittee(requestObj);
		return ResponseEntity.status(HttpStatus.OK).body(list);
	}

	@DeleteMapping("/committee/{id}")
	public ResponseEntity<Void> deleteCommittee(@PathVariable Long id) {
		logger.info("Request to delete a committee");
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode obj = mapper.createObjectNode();
		obj.put("id", id);
		obj.put("status", Constants.STATUS_DEACTIVE);
		 committeeService.updateCommittee(obj);
		return ResponseEntity.noContent()
				.headers(HeaderUtil.createEntityDeletionAlert("committee", false, "committee", id.toString())).build();
	}

	@GetMapping("/committee/{id}")
	public ResponseEntity<List<Committee>> getCommittee(@PathVariable Map<String, String>  id) {
		logger.info("Getting committee by id: " + id);
//			Committee committee = committeeService.getCommittee(id);
		List<Committee> list = committeeService.searchCommittee(id);
			return ResponseEntity.status(HttpStatus.OK).body(list);
	}

}