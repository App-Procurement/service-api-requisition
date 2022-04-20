package com.synectiks.procurement.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.synectiks.procurement.business.service.InvoiceService;
import com.synectiks.procurement.domain.Invoice;
import com.synectiks.procurement.repository.InvoiceRepository;
import com.synectiks.procurement.web.rest.TestUtil;

@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
@SpringBootTest
@ActiveProfiles("Invoice controller test")
@AutoConfigureMockMvc
@WithMockUser
class InvoiceControllerTest {

	@Autowired
	private InvoiceRepository invoiceRepository;

	@Autowired
	private MockMvc mockMvc;
	
	@Mock
	InvoiceService invoiceService;
	

	Invoice invoice = new Invoice();
	
	private ObjectMapper mapper = new ObjectMapper();

	@Test
	@Transactional
	@Order(1)
	public void testAddInvoiceController() throws IOException, Exception {

		ObjectNode obj = mapper.createObjectNode();
		
		obj.put("invoiceNo", "a");
		obj.put("modeOfPayment", "b");
		obj.put("txnRefNo", "c");
		obj.put("chequeOrDdNo", "d");
		obj.put("issuerBank", "e");
		obj.put("status", "f");
		obj.put("notes", "g");
		obj.put("documentId", "1");
		obj.put("quotationId", "1");
		
	
		int databaseSizeBeforeCreate = invoiceRepository.findAll().size();

	
		mockMvc.perform(post("/api/invoice").contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(obj)));

		List<Invoice> invoice1 = invoiceRepository.findAll();
		assertThat(invoice1).hasSize(databaseSizeBeforeCreate + 1);
		Invoice testinvoice = invoice1.get(invoice1.size() - 1);

		assertThat(testinvoice.getInvoiceNo()).isEqualTo("a");
		assertThat(testinvoice.getModeOfPayment()).isEqualTo("b");
		assertThat(testinvoice.getTxnRefNo()).isEqualTo("c");
		assertThat(testinvoice.getChequeOrDdNo()).isEqualTo("d");
		assertThat(testinvoice.getIssuerBank()).isEqualTo("e");
		assertThat(testinvoice.getStatus()).isEqualTo("f");
		assertThat(testinvoice.getNotes()).isEqualTo("g");
	}

	@Test
	@Transactional
	@Order(2)
	public void testUpdateinvoiceController() throws IOException, Exception {

//	    	Id Generated Automatically using MockMvc

		invoice.setInvoiceNo("a");
		invoice.setModeOfPayment("b");
		invoice.setNotes("c");

		invoiceRepository.saveAndFlush(invoice);

		// Initialize the database

		int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();

		// Update the invoice

		Invoice updatedInvoice = invoiceRepository.findById(invoice.getId()).get();
		// Disconnect from session so that the updates on updatedinvoice are not directly
		// saved in db

		updatedInvoice.setInvoiceNo("aa");
		updatedInvoice.setModeOfPayment("bb"); 
		updatedInvoice.setNotes("cc");

		mockMvc.perform(put("/api/invoice").contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(updatedInvoice))).andExpect(status().isOk());

		// Validate the invoice in the database
		List<Invoice> invoiceList = invoiceRepository.findAll();
		assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
		Invoice testinvoice = invoiceList.get(invoiceList.size() - 1);
		assertThat(testinvoice.getInvoiceNo()).isEqualTo("aa");
		assertThat(testinvoice.getModeOfPayment()).isEqualTo("bb");
		assertThat(testinvoice.getNotes()).isEqualTo("cc");
	}
	
	


	@Test
	@Transactional
	@Order(3)
	public void deleteinvoice() throws Exception {

//	    	Id Generated Automatically using MockMvc

		invoice.setStatus("Active");

		invoiceRepository.saveAndFlush(invoice);

		// Deactivate the invoice

		mockMvc.perform(delete("/api/invoice/{id}", invoice.getId()).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		// Validate the database contains one less item
		Optional<Invoice> invoice1 = invoiceRepository.findById(invoice.getId());
		assertThat(invoice1.get().getStatus()).isEqualTo("DEACTIVE");

	}
	
	
//	@Test
//	@Transactional
//	@Order(3)
//	public void testSearchinvoiceController() throws IOException, Exception {
//
////	    	Id Generated Automatically using MockMvc
//
//		invoice.setFirstName("a");
//		invoice.setMiddleName("b");
//		invoice.setLastName("c");
//		invoice.phoneNumber("d");
//		invoice.setEmail("e");
//		invoice.setAddress("f");
//		invoice.setZipCode("g");
//		invoice.setStatus("h");
//
//		invoiceRepository.saveAndFlush(invoice);
//
//		// Initialize the database
//
//		int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();
//
//		// Search the invoice
//
//	
//	}
//	
	

}
