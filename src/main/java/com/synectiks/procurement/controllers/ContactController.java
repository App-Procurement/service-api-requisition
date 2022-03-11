package com.synectiks.procurement.controllers;

import java.util.HashMap;
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
import com.synectiks.procurement.business.service.ContactService;
import com.synectiks.procurement.config.BusinessValidationCodes;
import com.synectiks.procurement.config.Constants;
import com.synectiks.procurement.domain.Contact;
import com.synectiks.procurement.web.rest.errors.DataNotFoundException;
import com.synectiks.procurement.web.rest.errors.IdNotFoundException;
import com.synectiks.procurement.web.rest.errors.NegativeIdException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "Contact APIs")
@RestController
@RequestMapping("/api")
public class ContactController {
	private static final Logger logger = LoggerFactory.getLogger(ContactController.class);

	@Autowired
	private ContactService contactService;

	@ApiOperation(value = "Create a new contact")
	@PostMapping("/contact")
	public ResponseEntity<Contact> addContact(@RequestBody ObjectNode obj){
		logger.info("Request to add new contact");
		try {
			Contact contact = contactService.addContact(obj);
			return ResponseEntity.status(HttpStatus.OK).body(contact);
		} catch (Exception e) {
			logger.error("Add contact failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}	
	}

	@ApiOperation(value = "Update an existing contact")
	@PutMapping("/contact")
	public ResponseEntity<Contact> updateContact(@RequestBody ObjectNode obj){
		logger.info("Request to updating contact");
		try {
			Contact contact = contactService.updateContact(obj);
			return ResponseEntity.status(HttpStatus.OK).body(contact);
		} catch (NegativeIdException e) {
			logger.error("Update contact failed. NegativeIdException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.NEGATIVE_ID_NOT_ALLOWED.value()).body(null);
		} catch (IdNotFoundException e) {
			logger.error("Update contact failed. IdNotFoundException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.ID_NOT_FOUND.value()).body(null);
		}catch (DataNotFoundException e) {
			logger.error("Update contact failed. DataNotFoundException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.DATA_NOT_FOUND.value()).body(null);
		}
		catch (Exception e) {
			logger.error("Update contact failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}
	
	}

	@ApiOperation(value = "Search contact")
	@GetMapping("/contact")
	public ResponseEntity<List<Contact>> searchContact(@RequestParam Map<String, String> requestObj) {
		logger.info("Request to get contact on given filter criteria");
		try {
			List<Contact> list = contactService.searchContact(requestObj);
			return ResponseEntity.status(HttpStatus.OK).body(list);
			
		} catch (NegativeIdException e) {
			logger.error("Update contact failed. NegativeIdException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.NEGATIVE_ID_NOT_ALLOWED.value()).body(null);
		} 
		catch (Exception e) {
			logger.error("Search contact failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}
			
	}
	
	@ApiOperation(value = "Delete a contact")
	@DeleteMapping("/contact/{id}")
	public ResponseEntity<Boolean> deletecontact(@PathVariable Long id) {
		logger.info("Request to delete a contact");
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode obj = mapper.createObjectNode();
		obj.put("id", id);
		obj.put("status", Constants.STATUS_DEACTIVE);
		try {
		Contact bu=contactService.updateContact(obj);
			if(Constants.STATUS_DEACTIVE.equalsIgnoreCase(bu.getStatus())){
				return ResponseEntity.status(HttpStatus.OK).body(Boolean.TRUE);
			}else {
				return ResponseEntity.status(BusinessValidationCodes.DELETION_FAILED.value()).body(Boolean.FALSE);
			}
		} 
		catch (NegativeIdException e) {
			logger.error("Delete contact failed. NegativeIdException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.NEGATIVE_ID_NOT_ALLOWED.value()).body(null);
		} catch (IdNotFoundException e) {
			logger.error("Delete contact failed. IdNotFoundException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.ID_NOT_FOUND.value()).body(null);
		} catch (DataNotFoundException e) {
			logger.error("Delete contact failed. DataNotFoundException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.DATA_NOT_FOUND.value()).body(null);
		}
		catch (Exception e) {
			logger.error("Delete contact failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}
			
	}
	
	
//	@ApiOperation(value = "Delete a contact")
//	@DeleteMapping("/contact/{id}")
//	public ResponseEntity<Boolean> deleteContact(@PathVariable Long id) {			
//			try {
//				boolean dlcontact = contactService.deleteContact(id);
//				if (dlcontact) {
//					return ResponseEntity.status(HttpStatus.OK).body(dlcontact);
//				} else {
//					return ResponseEntity.status(HttpStatus.OK).body(Boolean.FALSE);
//				} 
//				
//			} catch (NegativeIdException e) {
//				logger.error("Delete contact failed. NegativeIdException: ", e.getMessage());
//				return ResponseEntity.status(BusinessValidationCodes.NEGATIVE_ID_NOT_ALLOWED.value()).body(null);
//			} catch (IdNotFoundException e) {
//				logger.error("Delete contact failed. IdNotFoundException: ", e.getMessage());
//				return ResponseEntity.status(BusinessValidationCodes.ID_NOT_FOUND.value()).body(null);
//			} catch (DataNotFoundException e) {
//				logger.error("Delete contact failed. DataNotFoundException: ", e.getMessage());
//				return ResponseEntity.status(BusinessValidationCodes.DATA_NOT_FOUND.value()).body(null);
//			} catch (Exception e) {
//				logger.error("Delete contact failed. Exception: ", e);
//				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
//			}
//	}
//	
	@ApiOperation(value = "Search a contact by id")
	@GetMapping("/contact/{id}")
	public ResponseEntity<Contact> getContact(@PathVariable Long id) {
		logger.info("Getting contact by id: " + id);
		Map<String, String> con = new HashMap<>();
		try {
			Contact contact = null;
			con.put("id", String.valueOf(id));
			List<Contact> conList = contactService.searchContact(con);
			if(conList.size() > 0) {
				contact = conList.get(0);
			}
			return ResponseEntity.status(HttpStatus.OK).body(contact);
		}catch (NegativeIdException e) {
			logger.error("Getting contact by id failed. NegativeIdException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.NEGATIVE_ID_NOT_ALLOWED.value()).body(null);
		}catch (Exception e) {
			logger.error("Getting contact by id failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}
 }
}

