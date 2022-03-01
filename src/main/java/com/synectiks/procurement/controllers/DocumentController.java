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
import com.synectiks.procurement.business.service.DocumentService;
import com.synectiks.procurement.domain.Document;

import io.github.jhipster.web.util.HeaderUtil;

@RestController
@RequestMapping("/api")
public class DocumentController {
	private static final Logger logger = LoggerFactory.getLogger(DocumentController.class);

	@Autowired
	private DocumentService documentService;

	@PostMapping("/document")
	public ResponseEntity<Document> addDocument(@RequestBody ObjectNode obj){
		logger.info("Request to add a new document");
			Document document = documentService.addDocument(obj);
			return ResponseEntity.status(HttpStatus.OK).body(document);
	}

	@PutMapping("/document")
	public ResponseEntity<Document> updateDocument(@RequestBody ObjectNode obj){
		logger.info("Request to update a document");
			Document document = documentService.updateDocument(obj);
			return ResponseEntity.status(HttpStatus.OK).body(document);
	}

	@GetMapping("/document")
	public ResponseEntity<List<Document>> searchdocument(@RequestParam Map<String, String> requestObj) {
		logger.info("Request to get document on given filter criteria");
			List<Document> list = documentService.searchDocument(requestObj);
			return ResponseEntity.status(HttpStatus.OK).body(list);
	}

	@DeleteMapping("/document/{id}")
	public ResponseEntity<Void> deleteDocument(@PathVariable Long id) {
			documentService.deleteDocument(id);
			return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert("document", false, "document", id.toString())).build();
	}
	@GetMapping("/document/{id}")
	public ResponseEntity<Document> getDepartment(@PathVariable Long id) {
			Document document = documentService.getDocument(id);
			return ResponseEntity.status(HttpStatus.OK).body(document);
	}
}
