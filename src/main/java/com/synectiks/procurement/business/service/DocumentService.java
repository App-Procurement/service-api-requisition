package com.synectiks.procurement.business.service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.synectiks.procurement.config.Constants;
import com.synectiks.procurement.domain.Contact;
import com.synectiks.procurement.domain.Document;
import com.synectiks.procurement.domain.RequisitionLineItem;
import com.synectiks.procurement.repository.ContactRepository;
import com.synectiks.procurement.repository.DocumentRepository;
import com.synectiks.procurement.repository.RequisitionLineItemRepository;
import com.synectiks.procurement.web.rest.errors.DataNotFoundException;
import com.synectiks.procurement.web.rest.errors.IdNotFoundException;
import com.synectiks.procurement.web.rest.errors.NegativeIdException;

@Service
public class DocumentService {
	private static final Logger logger = LoggerFactory.getLogger(DocumentService.class);

	@Autowired
	private RequisitionLineItemRepository requisitionLineItemRepository;

	@Autowired
	private ContactRepository contactRepository;

	@Autowired
	private DocumentRepository documentRepository;

	public Document getDocument(Long id) {
		logger.info("Getting document by id: " + id);
		Optional<Document> oin = documentRepository.findById(id);
		if (oin.isPresent()) {
			logger.info("Document: " + oin.get());
			return oin.get();
		}
		logger.warn("Document not found");
		return null;
	}

	public Document addDocument(ObjectNode obj) {
		Document document = new Document();
		Optional<Contact> oc = contactRepository.findById(Long.parseLong(obj.get("contactId").asText()));
		if (oc.isPresent()) {
			document.setContact(oc.get());
		}

		Optional<RequisitionLineItem> orli = requisitionLineItemRepository
				.findById(Long.parseLong(obj.get("requisitionLineItemId").asText()));
		if (orli.isPresent()) {
			document.setRequisitionLineItem(orli.get());
		}

		if (obj.get("requisitionLineItemId") != null) {

		}
		if (obj.get("fileName") != null) {
			document.setFileName(obj.get("fileName").asText());
		}
		if (obj.get("fileType") != null) {
			document.setFileType(obj.get("fileType").asText());
		}
		if (obj.get("fileSize") != null) {
			document.setFileSize(obj.get("fileSize").asLong());
		}
		if (obj.get("localFilePath") != null) {
			document.setLocalFilePath(obj.get("localFilePath").asText());
		}
		if (obj.get("s3Bucket") != null) {
			document.sets3Bucket(obj.get("s3Bucket").asText());
		}
		if (obj.get("sourceOfOrigin") != null) {
			document.setSourceOfOrigin(obj.get("sourceOfOrigin").asText());
		}
		if (obj.get("user") != null) {
			document.setCreatedBy(obj.get("user").asText());
			document.setUpdatedBy(obj.get("user").asText());
		} else {
			document.setCreatedBy(Constants.SYSTEM_ACCOUNT);
			document.setUpdatedBy(Constants.SYSTEM_ACCOUNT);
		}
		Instant now = Instant.now();
		document.setCreatedOn(now);
		document.setUpdatedOn(now);

		document = documentRepository.save(document);

		return document;
	}

	public Document saveDocument(Document document) {
		document = documentRepository.save(document);
		logger.info("Document saved successfully: " + document.toString());

		return document;
	}

	public Document updateDocument(ObjectNode obj)
			throws IdNotFoundException, NegativeIdException, DataNotFoundException {

		if (org.apache.commons.lang3.StringUtils.isBlank(obj.get("id").asText())) {
			logger.error("Document could not be updated. Document id not found");
			throw new IdNotFoundException(Constants.ID_NOT_FOUND_ERROR_MESSAGE);
		}

		Long reqId = Long.parseLong(obj.get("id").asText());
		if (reqId < 0) {
			throw new NegativeIdException(Constants.NEGATIVE_ID_ERROR_MESSAGE);
		}

		Optional<Document> odc = documentRepository.findById(Long.parseLong(obj.get("id").asText()));
		if (!odc.isPresent()) {
			logger.error("Document could not be updated. Document not found");
			throw new DataNotFoundException(Constants.DATA_NOT_FOUND_ERROR_MESSAGE);
		}
		Document document = odc.get();

		if (obj.get("fileName") != null) {
			document.setFileName(obj.get("fileName").asText());
		}
		if (obj.get("status") != null) {
			document.setStatus(obj.get("status").asText());
		}
		if (obj.get("fileType") != null) {
			document.setFileType(obj.get("fileType").asText());
		}
		if (obj.get("fileSize") != null) {
			document.setFileSize(obj.get("fileSize").asLong());
		}
		if (obj.get("localFilePath") != null) {
			document.setLocalFilePath(obj.get("localFilePath").asText());
		}
		if (obj.get("s3Bucket") != null) {
			document.sets3Bucket(obj.get("s3Bucket").asText());
		}
		if (obj.get("sourceOfOrigin") != null) {
			document.setSourceOfOrigin(obj.get("sourceOfOrigin").asText());
		}

		if (obj.get("contactId") != null) {
			Optional<Contact> oc = contactRepository.findById(Long.parseLong(obj.get("contactId").asText()));

			if (oc.isPresent()) {
				document.setContact(oc.get());
			}
		}

		if (obj.get("requisitionLineItemId") != null) {
			
			Optional<RequisitionLineItem> orli = requisitionLineItemRepository
					.findById(Long.parseLong(obj.get("requisitionLineItemId").asText()));

			if (orli.isPresent()) {
				document.setRequisitionLineItem(orli.get());
			}
			
		}

		if (obj.get("user") != null) {
			document.setUpdatedBy(obj.get("user").asText());
		} else {
			document.setUpdatedBy(Constants.SYSTEM_ACCOUNT);
		}
		document.setUpdatedOn(Instant.now());
		document = documentRepository.save(document);
		logger.info("Updating document completed");
		return document;

	}

	public List<Document> searchDocument(@RequestParam Map<String, String> requestObj) throws NegativeIdException {
		logger.info("Request to search document on given filter criteria");
		Document document = new Document();

		boolean isFilter = false;

		if (requestObj.get("id") != null) {

			Long reqId = Long.parseLong(requestObj.get("id"));
			if (reqId < 0) {
				throw new NegativeIdException(Constants.NEGATIVE_ID_ERROR_MESSAGE);
			}

			document.setId(Long.parseLong(requestObj.get("id")));
			isFilter = true;
		}

		if (requestObj.get("fileName") != null) {
			document.setFileName(requestObj.get("fileName"));
			isFilter = true;
		}
		if (requestObj.get("fileType") != null) {
			document.setFileType(requestObj.get("fileType"));
			isFilter = true;
		}
		if (requestObj.get("fileSize") != null) {
			document.setFileSize(Long.valueOf(requestObj.get("fileSize")));
			isFilter = true;
		}
		if (requestObj.get("fileExt") != null) {
			document.setFileExt(requestObj.get("fileExt"));
			isFilter = true;
		}
		if (requestObj.get("storageLocation") != null) {
			document.setStorageLocation(requestObj.get("storageLocation"));
			isFilter = true;
		}
		if (requestObj.get("localFilePath") != null) {
			document.setLocalFilePath(requestObj.get("localFilePath"));
			isFilter = true;
		}
		if (requestObj.get("s3Bucket") != null) {
			document.sets3Bucket(requestObj.get("s3Bucket"));
			isFilter = true;
		}
		if (requestObj.get("s3Url") != null) {
			document.sets3Url(requestObj.get("s3Url"));
			isFilter = true;
		}
		if (requestObj.get("azureUrl") != null) {
			document.setAzureUrl(requestObj.get("azureUrl"));
			isFilter = true;
		}
		if (requestObj.get("sourceOfOrigin") != null) {
			document.setSourceOfOrigin(requestObj.get("sourceOfOrigin"));
			isFilter = true;
		}
		if (requestObj.get("sourceId") != null) {
			document.setSourceId(Long.valueOf(requestObj.get("sourceId")));
			isFilter = true;
		}
		if (requestObj.get("identifier") != null) {
			document.setIdentifier(requestObj.get("identifier"));
			isFilter = true;
		}
		if (requestObj.get("createdOn") != null) {
			Instant instant = Instant.parse(requestObj.get("createdOn"));
			document.setCreatedOn(instant);
			isFilter = true;
		}
		if (requestObj.get("createdBy") != null) {
			document.setCreatedBy(requestObj.get("createdBy"));
			isFilter = true;
		}
		if (requestObj.get("updatedOn") != null) {
			Instant instant = Instant.parse(requestObj.get("updatedOn"));
			document.setUpdatedOn(instant);
			isFilter = true;
		}
		if (requestObj.get("updatedBy") != null) {
			document.setUpdatedBy(requestObj.get("updatedBy"));
			isFilter = true;
		}
		List<Document> list = null;
		if (isFilter) {
			list = this.documentRepository.findAll(Example.of(document), Sort.by(Direction.DESC, "id"));
		} else {
			list = this.documentRepository.findAll(Sort.by(Direction.DESC, "id"));
		}
		logger.info("Document search completed. Total records: " + list.size());
		return list;
	}

	public boolean deleteDocument(Long id) throws IdNotFoundException, NegativeIdException, DataNotFoundException {

		if (id == null) {
			logger.error("Document could not be deleted. Document id not found");
			throw new IdNotFoundException(Constants.ID_NOT_FOUND_ERROR_MESSAGE);
		}
		if (id < 0) {
			throw new NegativeIdException(Constants.NEGATIVE_ID_ERROR_MESSAGE);
		}

		Optional<Document> document = documentRepository.findById(id);

		if (!document.isPresent()) {
			logger.error("Document could not be deleted. Document not found");
			throw new DataNotFoundException(Constants.DATA_NOT_FOUND_ERROR_MESSAGE);
		}
		documentRepository.deleteById(id);
		return true;
	}

}