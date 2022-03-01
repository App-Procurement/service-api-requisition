package com.synectiks.procurement.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.synectiks.procurement.business.service.CommitteeMembersService;
import com.synectiks.procurement.domain.CommitteeMember;

@RestController
@RequestMapping("/api")
public class CommitteeMembersController {
	private static final Logger logger = LoggerFactory.getLogger(CommitteeMembersController.class);

	@Autowired
	private CommitteeMembersService committeeMembersService;

	@RequestMapping(value = "/committeeMembers", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CommitteeMember> addCommitteeMembers(@RequestParam("obj") String obj,
			@RequestParam(name="file",required = false) MultipartFile file ) throws IOException, JSONException {
		logger.info("Request to add new committee member");
		CommitteeMember committeeMember=null;

		try {
			committeeMember = committeeMembersService.addCommitteeMember(obj, file);
			return ResponseEntity.status(HttpStatus.OK).body(committeeMember);
		} catch (IOException e) {
			logger.error("Add committee member failed. IOException: ", e);
			throw e;
		} catch (JSONException e) {
			logger.error("Add committee member failed. JSONException: ", e);
			throw e;
		}

	}

	@RequestMapping(value = "/committeeMembers", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CommitteeMember> updateCommitteeMembers(@RequestParam("obj") String obj,
			@RequestParam(name="file",required = false) MultipartFile file) throws IOException, JSONException {
		logger.info("Request to upde committee members");
		CommitteeMember committeeMember=null;
		try {
			committeeMember = committeeMembersService.updateCommitteeMembers(obj, file);
			return ResponseEntity.status(HttpStatus.OK).body(committeeMember);
		} catch (IOException e) {
			logger.error("Update committee member failed. IOException: ", e);
			throw e;
		} catch (JSONException e) {
			logger.error("Update committee member failed. JSONException: ", e);
			throw e;
		}
	}

	@GetMapping("/committeeMembers")
	public ResponseEntity<List<CommitteeMember>> searchCommitteeMembers(@RequestParam Map<String, String> requestObj)
			throws IOException {
		logger.info("Request to get list of committee on given filter criteria");

		List<CommitteeMember> list;
		try {
			list = committeeMembersService.searchCommitteeMembers(requestObj);
			return ResponseEntity.status(HttpStatus.OK).body(list);
		} catch (IOException e) {
			logger.error("Search committee members failed. IOException: ", e);
			throw e;
		}
	}

	@GetMapping("/committeeMembers/{id}")
	public ResponseEntity<List<CommitteeMember>> getCommitteeMembers(@PathVariable Map<String, String> id)throws IOException {
		logger.info("Getting committee members by id: " + id);
		List<CommitteeMember> committeeMembers;
		try {
			committeeMembers = committeeMembersService.searchCommitteeMembers(id);
			return ResponseEntity.status(HttpStatus.OK).body(committeeMembers);
		} catch (IOException e) {
			logger.error("Search committee members failed. IOException: ", e);
			throw e;
		}
		
		
	}

}