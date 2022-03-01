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
import com.synectiks.procurement.business.service.ContactService;
import com.synectiks.procurement.domain.Contact;
import com.synectiks.procurement.domain.Status;

import io.github.jhipster.web.util.HeaderUtil;

@RestController
@RequestMapping("/api")
public class ContactController {
	private static final Logger logger = LoggerFactory.getLogger(ContactController.class);

	@Autowired
	private ContactService contactService;

	@PostMapping("/contact")
	public ResponseEntity<Contact> addContact(@RequestBody ObjectNode obj){
		logger.info("Request to add new contact");
			Contact contact = contactService.addContact(obj);
			return ResponseEntity.status(HttpStatus.OK).body(contact);
	}

	@PutMapping("/contact")
	public ResponseEntity<Contact> updateContact(@RequestBody ObjectNode obj){
		logger.info("Request to updating contact");
			Contact contact = contactService.updateContact(obj);
			return ResponseEntity.status(HttpStatus.OK).body(contact);
	}

	@GetMapping("/contact")
	public ResponseEntity<List<Contact>> searchContact(@RequestParam Map<String, String> requestObj) {
		logger.info("Request to get contact on given filter criteria");
			List<Contact> list = contactService.searchContact(requestObj);
			return ResponseEntity.status(HttpStatus.OK).body(list);
	}

	@DeleteMapping("/contact/{id}")
	public ResponseEntity<Contact> deleteContact(@PathVariable Long id) {
			contactService.deleteContact(id);
			return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert("contact", false, "contact", id.toString())).build();
	}

	@GetMapping("/contact/{id}")
	public ResponseEntity<Contact> getContact(@PathVariable Long id) {
		logger.info("Getting contact by id: " + id);
			Contact contact = contactService.getContact(id);
			return ResponseEntity.status(HttpStatus.OK).body(contact);
	
	}
}
//testing......
