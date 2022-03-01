package com.synectiks.procurement.business.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.synectiks.procurement.config.Constants;
import com.synectiks.procurement.domain.Document;
import com.synectiks.procurement.domain.Invoice;
import com.synectiks.procurement.domain.InvoiceActivity;
import com.synectiks.procurement.domain.Quotation;
//import com.synectiks.procurement.domain.Requisition;
//import com.synectiks.procurement.domain.Roles;
//import com.synectiks.procurement.domain.Rules;
import com.synectiks.procurement.repository.DocumentRepository;
import com.synectiks.procurement.repository.InvoiceActivityRepository;
import com.synectiks.procurement.repository.InvoiceRepository;
import com.synectiks.procurement.repository.QuotationRepository;
//import com.synectiks.procurement.repository.RequisitionRepository;

@Service
public class InvoiceService {
	private static final Logger logger = LoggerFactory.getLogger(InvoiceService.class);

	@Autowired
	private InvoiceActivityRepository invoiceActivityRepository;

	@Autowired
	private InvoiceRepository invoiceRepository;

	@Autowired
	private QuotationRepository quotationRepository;

	@Autowired
	private DocumentRepository documentRepository;

//	@Autowired
//	private RolesService rolesService;
//
//	@Autowired
//	private RulesService rulesService;
//
//	@Autowired
//	private RequisitionRepository requisitionRepository;

	@Transactional
	public Invoice addInvoice(ObjectNode obj) {
		Invoice invoice = new Invoice();

		Optional<Document> odc = documentRepository.findById(Long.parseLong(obj.get("documentId").asText()));
		if (odc.isPresent()) {
			invoice.setDocument(odc.get());
		}

		Optional<Quotation> oqot = quotationRepository.findById(Long.parseLong(obj.get("quotationId").asText()));
		if (oqot.isPresent()) {
			invoice.setQuotation(oqot.get());
		}

		if (obj.get("invoiceNo") != null) {
			invoice.setInvoiceNo(obj.get("invoiceNo").asText());
		}

		if (obj.get("amount") != null) {
			invoice.setAmount(obj.get("amount").asInt());
		}
		if (obj.get("modeOfPayment") != null) {
			invoice.setModeOfPayment(obj.get("modeOfPayment").asText());
		}
		if (obj.get("txnRefNo") != null) {
			invoice.setTxnRefNo(obj.get("txnRefNo").asText());
		}
		if (obj.get("chequeOrDdNo") != null) {
			invoice.setChequeOrDdNo(obj.get("chequeOrDdNo").asText());
		}
		if (obj.get("issuerBank") != null) {
			invoice.setIssuerBank(obj.get("issuerBank").asText());
		}
		if (obj.get("chequeOrDdNo") != null) {
			invoice.setChequeOrDdNo(obj.get("chequeOrDdNo").asText());
		}
		if (obj.get("notes") != null) {
			invoice.setNotes(obj.get("notes").asText());
		}
		if (obj.get("status") != null) {
			invoice.setStatus(obj.get("status").asText());
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DEFAULT_DATE_FORMAT);
		if (obj.get("dueDate") != null) {
			LocalDate localDate = LocalDate.parse(obj.get("dueDate").asText(), formatter);
			invoice.setDueDate(localDate);
		} else {
			long millis = System.currentTimeMillis();
			java.sql.Date date = new java.sql.Date(millis);

			LocalDate datew = LocalDate.parse(date.toString());
			LocalDate localDate = datew.plusDays(Constants.DEFAULT_DUE_DAYS);
			invoice.setDueDate(localDate);
		}
		if (obj.get("user") != null) {
			invoice.setCreatedBy(obj.get("user").asText());
			invoice.setUpdatedBy(obj.get("user").asText());
		} else {
			invoice.setCreatedBy(Constants.SYSTEM_ACCOUNT);
			invoice.setUpdatedBy(Constants.SYSTEM_ACCOUNT);
		}
		Instant now = Instant.now();
		invoice.setCreatedOn(now);
		invoice.setUpdatedOn(now);
		invoice = invoiceRepository.save(invoice);
		logger.info("Invoice added successfully");

		if (invoice != null) {
			InvoiceActivity invoiceActivity = new InvoiceActivity();
			BeanUtils.copyProperties(invoice, invoiceActivity);
			invoiceActivity.setInvoiceId(invoice.getId());
			invoiceActivity = invoiceActivityRepository.save(invoiceActivity);
			logger.info("Invoice activity added successfully");
		}
		return invoice;
	}

	@Transactional
	public Invoice updateinvoice(ObjectNode obj){
		Optional<Invoice> ur = invoiceRepository.findById(Long.parseLong(obj.get("id").asText()));
		if (!ur.isPresent()) {
			logger.error("Invoice not found");
			return null;
		}
		Invoice invoice = ur.get();
		if (obj.get("invoiceNo") != null) {
			invoice.setInvoiceNo(obj.get("invoiceNo").asText());
		}

		if (obj.get("amount") != null) {
			invoice.setAmount(obj.get("amount").asInt());
		}

		if (obj.get("modeOfPayment") != null) {
			invoice.setModeOfPayment(obj.get("modeOfPayment").asText());
		}

		if (obj.get("txnRefNo") != null) {
			invoice.setTxnRefNo(obj.get("txnRefNo").asText());
		}

		if (obj.get("chequeOrDdNo") != null) {
			invoice.setChequeOrDdNo(obj.get("chequeOrDdNo").asText());
		}

		if (obj.get("issuerBank") != null) {
			invoice.setIssuerBank(obj.get("issuerBank").asText());
		}

		if (obj.get("chequeOrDdNo") != null) {
			invoice.setChequeOrDdNo(obj.get("chequeOrDdNo").asText());
		}

		if (obj.get("notes") != null) {
			invoice.setNotes(obj.get("notes").asText());
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DEFAULT_DATE_FORMAT);
		if (obj.get("dueDate") != null) {
			LocalDate localDate = LocalDate.parse(obj.get("dueDate").asText(), formatter);
			invoice.setDueDate(localDate);
		} else {
			long millis = System.currentTimeMillis();
			java.sql.Date date = new java.sql.Date(millis);

			LocalDate datew = LocalDate.parse(date.toString());
			LocalDate localDate = datew.plusDays(Constants.DEFAULT_DUE_DAYS);
			invoice.setDueDate(localDate);
		}

		if (obj.get("user") != null) {
			invoice.setUpdatedBy(obj.get("user").asText());
		} else {
			invoice.setUpdatedBy(Constants.SYSTEM_ACCOUNT);
		}
		invoice.setUpdatedOn(Instant.now());
		invoice = invoiceRepository.save(invoice);
		logger.info("Updating invoice completed" + invoice.toString());

		if (invoice != null) {
			InvoiceActivity invoiceActivity = new InvoiceActivity();
			BeanUtils.copyProperties(invoice, invoiceActivity);
			invoiceActivity.setInvoiceId(invoice.getId());
			invoiceActivity = invoiceActivityRepository.save(invoiceActivity);
			logger.info("Invoice activity added successfully");
		}
		return invoice;

	}

	public List<Invoice> searchinvoice(Map<String, String> requestObj) {
		logger.info("Request to get invoice on given filter criteria");
		Invoice invoice = new Invoice();
		boolean isFilter = false;
		if (requestObj.get("id") != null) {
			invoice.setId(Long.parseLong(requestObj.get("id")));
			isFilter = true;
		}

		if (requestObj.get("amount") != null) {
			invoice.setAmount(Integer.valueOf(requestObj.get("amount")));
			isFilter = true;
		}
		if (requestObj.get("invoiceNo") != null) {
			invoice.setInvoiceNo(requestObj.get("invoiceNo"));
			isFilter = true;
		}
		if (requestObj.get("modeOfPayment") != null) {
			invoice.setModeOfPayment(requestObj.get("modeOfPayment"));
			isFilter = true;
		}
		if (requestObj.get("txnRefNo") != null) {
			invoice.setTxnRefNo(requestObj.get("txnRefNo"));
			isFilter = true;
		}
		if (requestObj.get("chequeOrDdNo") != null) {
			invoice.setChequeOrDdNo(requestObj.get("chequeOrDdNo"));
			isFilter = true;
		}
		if (requestObj.get("issuerBank") != null) {
			invoice.setIssuerBank(requestObj.get("issuerBank"));
			isFilter = true;
		}
		if (requestObj.get("notes") != null) {
			invoice.setNotes(requestObj.get("notes"));
			isFilter = true;
		}
		if (requestObj.get("createdOn") != null) {
			Instant instant = Instant.parse(requestObj.get("createdOn"));
			invoice.setCreatedOn(instant);
			isFilter = true;
		}
		if (requestObj.get("createdBy") != null) {
			invoice.setCreatedBy(requestObj.get("createdBy"));
			isFilter = true;
		}
		if (requestObj.get("updatedOn") != null) {
			Instant instant = Instant.parse(requestObj.get("updatedOn"));
			invoice.setUpdatedOn(instant);
			isFilter = true;
		}
	    if (requestObj.get("updatedBy") != null) {
	    	invoice.setUpdatedBy(requestObj.get("updatedBy"));
			isFilter = true;
		}
		List<Invoice> list = null;
		if (isFilter) {
			list = this.invoiceRepository.findAll(Example.of(invoice), Sort.by(Direction.DESC, "id"));
		} else {
			list = this.invoiceRepository.findAll(Sort.by(Direction.DESC, "id"));
		}
		for (Invoice inv : list) {
			InvoiceActivity ca = new InvoiceActivity();
			ca.setInvoiceId(inv.getId());
			List<InvoiceActivity> caList = invoiceActivityRepository.findAll(Example.of(ca));
			inv.setActivityList(caList);
		}

		logger.info("Invoice search completed. Total records: " + list.size());
		return list;

	}

	public void deleteInvoice(Long id) {
		documentRepository.deleteById(id);
	}

	public Invoice getInvoice(Long id) {
		logger.info("Getting invoice by id: " + id);
		Optional<Invoice> oin = invoiceRepository.findById(id);
		if (oin.isPresent()) {
			logger.info("Invoice: " + oin.get().toString());
			return oin.get();
		}
		logger.warn("Invoice not found");
		return null;
	}

//	public boolean approveInvoice(ObjectNode obj) throws JSONException {
//		logger.info("Getting requisition by id: " + obj);
//
//		try {
//			if (obj.get("invoiceId") == null) {
//				logger.error("invoice id not found. Cannot approve invoice");
//				return false;
//			}
//
//			if (obj.get("roleName") == null) {
//				logger.error("Role not found. Cannot approve invoice");
//				return false;
//			}
//
//			Optional<Invoice> req = invoiceRepository.findById(obj.get("invoiceId").asLong());
//			if (!req.isPresent()) {
//				logger.error("invoice not found. Cannot approve invoice");
//				return false;
//			}
//
//			Roles role = rolesService.getRolesByName(obj.get("roleName").asText());
//			if (role == null) {
//				logger.error("Given role " + obj.get("roleName").asText() + " not found. Cannot approve invoice ");
//				return false;
//			}
//			
//			Optional<Requisition> requ = requisitionRepository.findById(obj.get("requisitionId").asLong());
//            if (!requ.isPresent()) {
//				logger.error("Requision not found. Cannot approve requisition.");
//				return false;
//			}
//            
//			Requisition requisition = requ.get();
//			Rules rule = rulesService.getRulesByRoleAndRuleName(role, Constants.RULE_APPROVE_INVOICE);
//			if (rule == null) {
//				logger.error("Approval rule not found. Cannot approve invoice");
//				return false;
//			}
//
//			JSONObject jsonObject = new JSONObject(rule.getRule());
//
//			int price = 0;
//			if (requisition.getTotalPrice() != null) {
//				price = requisition.getTotalPrice().intValue();
//			}
//
//			int minRulePrice = 0;
//			int maxRulePrice = 0;
//
//			try {
//				minRulePrice = jsonObject.getInt("min");
//			} catch (Exception e) {
//				logger.error("Minimum price rule not found. Cannot approve invoice . Exception: ", e);
//				return false;
//			}
//
//			if (jsonObject.get("max") != null) {
//				try {
//					maxRulePrice = jsonObject.getInt("max");
//				} catch (Exception e) {
//					logger.error("Incorrect maximum price rule. Cannot approve invoice . Exception: ", e);
//					return false;
//				}
//			}
//
//			if (price >= minRulePrice && jsonObject.get("max") == null) {
//				requisition.setStatus(Constants.PROGRESS_STAGE_APPROVED);
//			}
//
//			if (price >= minRulePrice && jsonObject.get("max") != null && price <= maxRulePrice) {
//				requisition.setStatus(Constants.PROGRESS_STAGE_APPROVED);
//			}
//
//			requisition = requisitionRepository.save(requisition);
//			return true;
//		} catch (Exception e) {
//			logger.error("Approve invoice  failed. Exception: ", e);
//			return false;
//		}
//
//	}

}
