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
import com.synectiks.procurement.business.service.InvoiceService;
import com.synectiks.procurement.config.BusinessValidationCodes;
import com.synectiks.procurement.domain.Invoice;
import com.synectiks.procurement.web.rest.errors.DataNotFoundException;
import com.synectiks.procurement.web.rest.errors.IdNotFoundException;
import com.synectiks.procurement.web.rest.errors.NegativeIdException;

import io.github.jhipster.web.util.HeaderUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "Invoice APIs")
@RestController
@RequestMapping("/api")
public class InvoiceController {
	private static final Logger logger = LoggerFactory.getLogger(InvoiceController.class);

	@Autowired
	private InvoiceService invoiceService;

	@ApiOperation(value = "Create a new invoice")
	@PostMapping("/invoice")
	public ResponseEntity<Invoice> addInvoice(@RequestBody ObjectNode obj) {
		logger.info("Request to add a new invoice");
		try {
			Invoice invoice = invoiceService.addInvoice(obj);
			return ResponseEntity.status(HttpStatus.OK).body(invoice);
		} catch (Exception e) {
			logger.error("Add invoice failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}

	}

	@ApiOperation(value = "Update an existing invoice")
	@PutMapping("/invoice")
	public ResponseEntity<Invoice> updateinvoice(@RequestBody ObjectNode obj) {
		logger.info("Request to update an invoice");

		try {
			Invoice invoice = invoiceService.updateinvoice(obj);
			return ResponseEntity.status(HttpStatus.OK).body(invoice);
		} catch (NegativeIdException e) {
			logger.error("Update invoice failed. NegativeIdException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.NEGATIVE_ID_NOT_ALLOWED.value()).body(null);
		} catch (IdNotFoundException e) {
			logger.error("Update invoice failed. IdNotFoundException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.ID_NOT_FOUND.value()).body(null);
		} catch (DataNotFoundException e) {
			logger.error("Update invoice failed. DataNotFoundException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.DATA_NOT_FOUND.value()).body(null);
		} catch (Exception e) {
			logger.error("Update invoice failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}
	}

	@ApiOperation(value = "Search invoice")
	@GetMapping("/invoice")
	public ResponseEntity<List<Invoice>> searchinvoice(@RequestParam Map<String, String> requestObj) {
		logger.info("Request to get list of invoices on given filter criteria");
		try {
			List<Invoice> list = invoiceService.searchinvoice(requestObj);
			return ResponseEntity.status(HttpStatus.OK).body(list);
		} catch (NegativeIdException e) {
			logger.error("Search invoices failed. NegativeIdException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.NEGATIVE_ID_NOT_ALLOWED.value()).body(null);
		} catch (Exception e) {
			logger.error("Search invoices failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}

	}

	@ApiOperation(value = "Delete a invoice")
	@DeleteMapping("/invoice/{id}")
	public ResponseEntity<Void> deleteInvoice(@PathVariable Long id) {
		invoiceService.deleteInvoice(id);
		return ResponseEntity.noContent()
				.headers(HeaderUtil.createEntityDeletionAlert("invoice", false, "invoice", id.toString())).build();
	}

	@ApiOperation(value = "Search a invoice by id")
	@GetMapping("/invoice/{id}")
	public ResponseEntity<Invoice> getInvoice(@PathVariable Long id) {
		logger.info("Getting invoice by id: " + id);

		Map<String, String> invObj = new HashMap<>();
		try {
			Invoice invoice = null;
			invObj.put("id", String.valueOf(id));
			List<Invoice> invList;

			invList = invoiceService.searchinvoice(invObj);
			if (invList.size() > 0) {
				invoice = invList.get(0);
			}
			return ResponseEntity.status(HttpStatus.OK).body(invoice);
		} catch (NegativeIdException e) {
			logger.error("Search invoice failed. NegativeIdException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.NEGATIVE_ID_NOT_ALLOWED.value()).body(null);
		} catch (Exception e) {
			logger.error("Search invoice failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}

	}

//	@PostMapping("/approveInvoice")
//	public ResponseEntity<Status> approveInvoice(@RequestBody ObjectNode obj) throws JSONException {
//		logger.info("Request to approve a invoice");
//
//		Status st = new Status();
//		try {
////			boolean updateFlag = invoiceService.approveInvoice(obj);
//			if (updateFlag) {
//				st.setCode(HttpStatus.OK.value());
//				st.setType("SUCCESS");
//				st.setMessage("Invoice  approved successfully");
//			} else {
//				st.setCode(HttpStatus.OK.value());
//				st.setType("Fialed");
//				st.setMessage("Invoice could not be updated");
//			}
//			return ResponseEntity.status(HttpStatus.OK).body(st);
//		} catch (Exception e) {
//			logger.error("Approve invoice failed");
//			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
//			st.setType("ERROR");
//			st.setMessage("Approve invoice failed");
//			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
//		}
//	}

}
