package com.synectiks.procurement.business.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.synectiks.procurement.config.Constants;
import com.synectiks.procurement.domain.Document;
import com.synectiks.procurement.domain.PurchaseOrder;
import com.synectiks.procurement.domain.Quotation;
import com.synectiks.procurement.domain.QuotationActivity;
import com.synectiks.procurement.domain.Vendor;
import com.synectiks.procurement.repository.QuotationActivityRepository;
import com.synectiks.procurement.repository.QuotationRepository;

@Service
public class QuotationService {

	private static final Logger logger = LoggerFactory.getLogger(QuotationService.class);

	@Autowired
	private QuotationRepository quotationRepository;

	@Autowired
	private QuotationActivityRepository quotationActivityRepository;

	@Autowired
	DocumentService documentService;

	@Autowired
	VendorService vendorService;

	@Autowired
	PurchaseOrderService purchaseOrderService;

	public Quotation getQuotation(Long id) {
		logger.info("Getting quotation by id: " + id);
		Optional<Quotation> ovn = quotationRepository.findById(id);
		if (ovn.isPresent()) {
			logger.info("Quotation: " + ovn.get().toString());
			return ovn.get();
		}
		logger.warn("Quotation not found");
		return null;
	}

	@Transactional
	public Quotation addQuotation(ObjectNode obj) {
		Quotation quotation = new Quotation();
		if (obj.get("documentId") != null) {
			Document doc = documentService.getDocument(Long.parseLong(obj.get("documentId").asText()));
			if (doc == null) {
				logger.error("Document not found. Quotation cannot be added");
				return null;
			}
			quotation.setDocument(doc);
		} else {
			logger.error("Document not provided. Quotation cannot be added");
		}

		if (obj.get("vendorId") != null) {
			Vendor vendor = vendorService.getVendor(Long.parseLong(obj.get("vendorId").asText()));
			if (vendor == null) {
				logger.error("Vendor not found. Quotation cannot be added");
				return null;
			}
			quotation.setVendor(vendor);
		} else {
			logger.error("Vendor not provided. Quotation cannot be added");
			return null;
		}
		if (obj.get("purchaseOrderId") != null) {
			PurchaseOrder purchaseOrder = purchaseOrderService
					.getPurchaseOrder(Long.parseLong(obj.get("purchaseOrderId").asText()));
			if (purchaseOrder == null) {
				logger.error("purchaseOrder not found. Quotation cannot be added");
				return null;
			}
			quotation.setPurchaseOrder(purchaseOrder);
		} else {
			logger.error("purchase order id not provided. Quotation cannot be added");
			return null;
		}

		if (obj.get("quotationNo") != null) {
			quotation.setQuotationNo(obj.get("quotationNo").asText());
		}

		if (obj.get("notes") != null) {
			quotation.setNotes(obj.get("notes").asText());
		}
		if (obj.get("status") != null) {
			quotation.setStatus(obj.get("status").asText());
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DEFAULT_DATE_FORMAT);
		if (obj.get("dueDate") != null) {
			LocalDate localDate = LocalDate.parse(obj.get("dueDate").asText(), formatter);
			quotation.setDueDate(localDate);
		} else {
			long millis = System.currentTimeMillis();
			java.sql.Date date = new java.sql.Date(millis);

			LocalDate datew = LocalDate.parse(date.toString());
			LocalDate localDate = datew.plusDays(Constants.DEFAULT_DUE_DAYS);
			quotation.setDueDate(localDate);
		}
		if (obj.get("user") != null) {
			quotation.setCreatedBy(obj.get("user").asText());
			quotation.setUpdatedBy(obj.get("user").asText());
		} else {
			quotation.setCreatedBy(Constants.SYSTEM_ACCOUNT);
			quotation.setUpdatedBy(Constants.SYSTEM_ACCOUNT);
		}

		Instant now = Instant.now();
		quotation.setCreatedOn(now);
		quotation.setUpdatedOn(now);

		quotation = quotationRepository.save(quotation);

		logger.info("New quotation added successfully");
		if (quotation != null) {
			QuotationActivity quotationActivity = new QuotationActivity();
			BeanUtils.copyProperties(quotation, quotationActivity);
			quotationActivity.setQuotationId(quotation.getId());
			quotationActivity = quotationActivityRepository.save(quotationActivity);
			logger.info("Quotation activity added");
		}
		return quotation;
	}

	@Transactional
	public Quotation updateQuotation(ObjectNode obj) {
		logger.info("Updating quotation");
		Optional<Quotation> ur = quotationRepository.findById(Long.parseLong(obj.get("id").asText()));
		if (!ur.isPresent()) {
			logger.error("Quotation not found. Cannot update quotation");
			return null;
		}

		Quotation quotation = ur.get();

		if (obj.get("quotationNo") != null) {
			quotation.setQuotationNo(obj.get("quotationNo").asText());
		}
		if (obj.get("notes") != null) {
			quotation.setNotes(obj.get("notes").asText());
		}

		if (obj.get("status") != null) {
			quotation.setStatus(obj.get("status").asText());
		}

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DEFAULT_DATE_FORMAT);
		if (obj.get("dueDate") != null) {
			LocalDate localDate = LocalDate.parse(obj.get("dueDate").asText(), formatter);
			quotation.setDueDate(localDate);
		} else {
			long millis = System.currentTimeMillis();
			java.sql.Date date = new java.sql.Date(millis);

			LocalDate datew = LocalDate.parse(date.toString());
			LocalDate localDate = datew.plusDays(Constants.DEFAULT_DUE_DAYS);
			quotation.setDueDate(localDate);
		}

		if (obj.get("user") != null) {
			quotation.setUpdatedBy(obj.get("user").asText());
		} else {
			quotation.setUpdatedBy(Constants.SYSTEM_ACCOUNT);
		}

		quotation.setUpdatedOn(Instant.now());
		quotation = quotationRepository.save(quotation);
		logger.info("Quotation updated");
		if (quotation != null) {
			QuotationActivity quotationActivity = new QuotationActivity();
			BeanUtils.copyProperties(quotation, quotationActivity);
			quotationActivity.setQuotationId(quotation.getId());
			quotationActivity = quotationActivityRepository.save(quotationActivity);
			logger.info("Quotation activity added");
		}
		return quotation;
	}

	public List<Quotation> searchQuotation(Map<String, String> requestObj) {
		logger.info("Searching quotations");
		Quotation quotation = new Quotation();

		boolean isFilter = false;
		if (requestObj.get("id") != null) {
			quotation.setId(Long.parseLong(requestObj.get("id")));
			isFilter = true;
		}
		if (requestObj.get("quotationNo") != null) {
			quotation.setQuotationNo(requestObj.get("quotationNo"));
			isFilter = true;
		}
		if (requestObj.get("status") != null) {
			quotation.setStatus(requestObj.get("status"));
			isFilter = true;
		}

		if (!StringUtils.isBlank(requestObj.get("documentId"))) {
			Document doc = documentService.getDocument(Long.parseLong(requestObj.get("documentId")));
			if (doc != null) {
				quotation.setDocument(doc);
			}
			isFilter = true;
		}

		if (!StringUtils.isBlank(requestObj.get("vendorId"))) {
			Vendor vendor = vendorService.getVendor(Long.parseLong(requestObj.get("vendorId")));
			if (vendor != null) {
				quotation.setVendor(vendor);
			}
			isFilter = true;
		}

		if (!StringUtils.isBlank(requestObj.get("purchaseOrderId"))) {
			PurchaseOrder purchaseOrder = purchaseOrderService
					.getPurchaseOrder(Long.parseLong(requestObj.get("purchaseOrderId")));
			if (purchaseOrder != null) {
				quotation.setPurchaseOrder(purchaseOrder);
			}
			isFilter = true;
		}

		if (requestObj.get("notes") != null) {
			quotation.setNotes(requestObj.get("notes"));
			isFilter = true;
		}

		if (requestObj.get("createdOn") != null) {
			Instant instant = Instant.parse(requestObj.get("createdOn"));
			quotation.setCreatedOn(instant);
			isFilter = true;
		}
		if (requestObj.get("createdBy") != null) {
			quotation.setCreatedBy(requestObj.get("createdBy"));
			isFilter = true;
		}
		if (requestObj.get("updatedOn") != null) {
			Instant instant = Instant.parse(requestObj.get("updatedOn"));
			quotation.setUpdatedOn(instant);
			isFilter = true;
		}
		if (requestObj.get("updatedBy") != null) {
			quotation.setUpdatedBy(requestObj.get("updatedBy"));
			isFilter = true;
		}

		List<Quotation> list = null;
		if (isFilter) {
			list = this.quotationRepository.findAll(Example.of(quotation), Sort.by(Direction.DESC, "id"));
		} else {
			list = this.quotationRepository.findAll(Sort.by(Direction.DESC, "id"));
		}
		for (Quotation qou : list) {
			QuotationActivity ca = new QuotationActivity();
			ca.setQuotationId(qou.getId());
			List<QuotationActivity> caList = quotationActivityRepository.findAll(Example.of(ca));
			qou.setActivityList(caList);
		}

		logger.info("Quotation search completed. Total records: " + list.size());

		return list;

	}

	public void deleteQuotation(Long id) {
		quotationRepository.deleteById(id);
		logger.info("Quotation deleted successfully");
	}
}
