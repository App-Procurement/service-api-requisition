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
import com.synectiks.procurement.business.service.QuotationService;
import com.synectiks.procurement.domain.Quotation;
import com.synectiks.procurement.repository.QuotationRepository;
import com.synectiks.procurement.web.rest.TestUtil;

@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
@SpringBootTest
@ActiveProfiles("Quotation controller test")
@AutoConfigureMockMvc
@WithMockUser
class QuotationControllerTest {

	@Autowired
	private QuotationRepository quotationRepository;

	@Autowired
	private MockMvc mockMvc;
	
	@Mock
	QuotationService quotationService;
	

	Quotation quotation = new Quotation();
	
	private ObjectMapper mapper = new ObjectMapper();

	@Test
	@Transactional
	@Order(1)
	public void testAddQuotationController() throws IOException, Exception {
		
		ObjectNode obj = mapper.createObjectNode();
 
		obj.put("quotationNo", "4");
		obj.put("status", "b");
		obj.put("documentId", "1");
		obj.put("vendorId", "1");
		obj.put("purchaseOrderId", "1");

		int databaseSizeBeforeCreate = quotationRepository.findAll().size();

	
		mockMvc.perform(post("/api/quotation").contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(quotation)));

		List<Quotation> quotation1 = quotationRepository.findAll();
		assertThat(quotation1).hasSize(databaseSizeBeforeCreate);  // it will be same because document, vendor and purchaseOrder are not present

	}

	@Test
	@Transactional
	@Order(2)
	public void testUpdatequotationController() throws IOException, Exception {

//	    	Id Generated Automatically using MockMvc

		quotation.setQuotationNo("a");
		quotation.setStatus("h");

		quotationRepository.saveAndFlush(quotation);

		// Initialize the database

		int databaseSizeBeforeUpdate = quotationRepository.findAll().size();

		// Update the quotation

		Quotation updatedquotation =  quotationRepository.findById(quotation.getId()).get();
		// Disconnect from session so that the updates on updatedquotation are not directly
		// saved in db

		updatedquotation.setQuotationNo("aa");
		updatedquotation.setStatus("hh");

			mockMvc.perform(put("/api/quotation").contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(updatedquotation))).andExpect(status().isOk());

		// Validate the quotation in the database
		List<Quotation> quotationList = quotationRepository.findAll();
		assertThat(quotationList).hasSize(databaseSizeBeforeUpdate);
		Quotation testquotation = quotationList.get(quotationList.size() - 1);
		assertThat(testquotation.getQuotationNo()).isEqualTo("aa");
		assertThat(testquotation.getStatus()).isEqualTo("hh");
		
	}
	
	


	@Test
	@Transactional
	@Order(3)
	public void deletequotation() throws Exception {

//	    	Id Generated Automatically using MockMvc

		quotation.setStatus("aa");
		
		quotationRepository.saveAndFlush(quotation);

		// Deactivate the quotation

		mockMvc.perform(delete("/api/quotation/{id}", quotation.getId()).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		// Validate the database contains one less item
		Optional<Quotation> quotation1 = quotationRepository.findById(quotation.getId());
		assertThat(quotation1.get().getStatus()).isEqualTo("DEACTIVE");

	}
	
	
//	@Test
//	@Transactional
//	@Order(3)
//	public void testSearchquotationController() throws IOException, Exception {
//
////	    	Id Generated Automatically using MockMvc
//
//		quotation.setFirstName("a");
//		quotation.setMiddleName("b");
//		quotation.setLastName("c");
//		quotation.phoneNumber("d");
//		quotation.setEmail("e");
//		quotation.setAddress("f");
//		quotation.setZipCode("g");
//		quotation.setStatus("h");
//
//		quotationRepository.saveAndFlush(quotation);
//
//		// Initialize the database
//
//		int databaseSizeBeforeUpdate = quotationRepository.findAll().size();
//
//		// Search the quotation
//
//	
//	}
//	
	

}
