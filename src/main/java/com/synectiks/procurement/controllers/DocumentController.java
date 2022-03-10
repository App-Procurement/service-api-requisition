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
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.synectiks.procurement.business.service.DocumentService;
import com.synectiks.procurement.config.BusinessValidationCodes;
import com.synectiks.procurement.domain.Document;
import com.synectiks.procurement.web.rest.errors.DataNotFoundException;
import com.synectiks.procurement.web.rest.errors.IdNotFoundException;
import com.synectiks.procurement.web.rest.errors.NegativeIdException;

@RestController
@RequestMapping("/api")
public class DocumentController {
	private static final Logger logger = LoggerFactory.getLogger(DocumentController.class);

	@Autowired
	private DocumentService documentService;

	@PostMapping("/document")
	public ResponseEntity<Document> addDocument(@RequestBody ObjectNode obj) {
		logger.info("Request to add a new document");
		try {
			Document document = documentService.addDocument(obj);
			return ResponseEntity.status(HttpStatus.OK).body(document);
		} catch (Exception e) {
			logger.error("Update document failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}
	}

	@PutMapping("/document")
	public ResponseEntity<Document> updateDocument(@RequestBody ObjectNode obj) {
		logger.info("Request to update a document");
		try {
			Document document = documentService.updateDocument(obj);
			return ResponseEntity.status(HttpStatus.OK).body(document);
		} catch (NegativeIdException e) {
			logger.error("Update requisition failed. NegativeIdException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.NEGATIVE_ID_NOT_ALLOWED.value()).body(null);
		} catch (IdNotFoundException e) {
			logger.error("Update requisition failed. IdNotFoundException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.ID_NOT_FOUND.value()).body(null);
		} catch (DataNotFoundException e) {
			logger.error("Update requisition failed. DataNotFoundException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.DATA_NOT_FOUND.value()).body(null);
		} catch (Exception e) {
			logger.error("Update requisition failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}
	}

	@GetMapping("/document")
	public ResponseEntity<List<Document>> searchdocument(@RequestParam Map<String, String> requestObj) {
		logger.info("Request to get document on given filter criteria");
		try {
			List<Document> list = documentService.searchDocument(requestObj);
			return ResponseEntity.status(HttpStatus.OK).body(list);
		} catch (NegativeIdException e) {
			logger.error("Search requisition failed. NegativeIdException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.NEGATIVE_ID_NOT_ALLOWED.value()).body(null);
		} catch (Exception e) {
			logger.error("Search requisition failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}
	}

	@DeleteMapping("/document/{id}")
	public ResponseEntity<Boolean> deleteDocument(@PathVariable Long id) {

		logger.info("Request to delete a document");
		try {
			boolean delDocument = documentService.deleteDocument(id);
			if (delDocument) {
				return ResponseEntity.status(HttpStatus.OK).body(Boolean.TRUE);
			} else {
				return ResponseEntity.status(BusinessValidationCodes.DELETION_FAILED.value()).body(Boolean.FALSE);
			}
		} catch (NegativeIdException e) {
			logger.error("Delete document failed. NegativeIdException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.NEGATIVE_ID_NOT_ALLOWED.value()).body(null);
		} catch (IdNotFoundException e) {
			logger.error("Delete document failed. IdNotFoundException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.ID_NOT_FOUND.value()).body(null);
		} catch (DataNotFoundException e) {
			logger.error("Delete document failed. DataNotFoundException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.DATA_NOT_FOUND.value()).body(null);
		} catch (Exception e) {
			logger.error("Delete document failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}
	}

	@GetMapping("/document/{id}")
	public ResponseEntity<Document> getDepartment(@PathVariable Long id) {

		Map<String, String> venObj = new HashMap<>();
		try {
			Document document = null;
			venObj.put("id", String.valueOf(id));
			List<Document> venList;

			venList = documentService.searchDocument(venObj);
			if (venList.size() > 0) {
				document = venList.get(0);
			}
			return ResponseEntity.status(HttpStatus.OK).body(document);
		} catch (NegativeIdException e) {
			logger.error("Search document failed. NegativeIdException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.NEGATIVE_ID_NOT_ALLOWED.value()).body(null);
		} catch (Exception e) {
			logger.error("Search document failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}

	}
}
