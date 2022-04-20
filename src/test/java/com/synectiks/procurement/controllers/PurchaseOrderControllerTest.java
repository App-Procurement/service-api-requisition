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
import com.synectiks.procurement.business.service.PurchaseOrderService;
import com.synectiks.procurement.domain.PurchaseOrder;
import com.synectiks.procurement.repository.PurchaseOrderRepository;
import com.synectiks.procurement.web.rest.TestUtil;

@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
@SpringBootTest
@ActiveProfiles("PurchaseOrder controller test")
@AutoConfigureMockMvc
@WithMockUser
class PurchaseOrderControllerTest {

	@Autowired
	private PurchaseOrderRepository purchaseOrderRepository;

	@Autowired
	private MockMvc mockMvc;
	
	@Mock
	PurchaseOrderService purchaseOrderService;
	

	PurchaseOrder purchaseOrder = new PurchaseOrder();
	
	private ObjectMapper mapper = new ObjectMapper();

	@Test
	@Transactional
	@Order(1)
	public void testAddpurchaseOrderController() throws IOException, Exception {

		ObjectNode obj = mapper.createObjectNode();
		obj.put("poNo", "1234");
		obj.put("dueDate","2021-07-21");
		obj.put("termsAndConditions","aaa");
		obj.put("notes","bbbb");
		obj.put("requisitionId","1");
		int databaseSizeBeforeCreate = purchaseOrderRepository.findAll().size();

	
		mockMvc.perform(post("/api/purchaseOrder").contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(obj)));

		List<PurchaseOrder> purchaseOrder1 = purchaseOrderRepository.findAll();
		assertThat(purchaseOrder1).hasSize(databaseSizeBeforeCreate + 1);
		PurchaseOrder testpurchaseOrder = purchaseOrder1.get(purchaseOrder1.size() - 1);

		assertThat(testpurchaseOrder.getPoNo()).isEqualTo("1234");
		assertThat(testpurchaseOrder.getDueDate()).isEqualTo("2021-07-21");
		assertThat(testpurchaseOrder.getTermsAndConditions()).isEqualTo("aaa");
		assertThat(testpurchaseOrder.getNotes()).isEqualTo("bbbb");
		}

	@Test
	@Transactional
	@Order(2)
	public void testUpdatepurchaseOrderController() throws IOException, Exception {

//	    	Id Generated Automatically using MockMvc

		purchaseOrder.setPoNo("1");
	
		purchaseOrder.setTermsAndConditions("c");
		purchaseOrder.setNotes("d");
		purchaseOrder.setStatus("h");

		purchaseOrderRepository.saveAndFlush(purchaseOrder);

		// Initialize the database

		int databaseSizeBeforeUpdate = purchaseOrderRepository.findAll().size();

		// Update the purchaseOrder

		PurchaseOrder updatedPurchaseOrder = purchaseOrderRepository.findById(purchaseOrder.getId()).get();
		// Disconnect from session so that the updates on updatedpurchaseOrder are not directly
		// saved in db

		updatedPurchaseOrder.setTermsAndConditions("cc");
		updatedPurchaseOrder.setNotes("dd");
		updatedPurchaseOrder.setStatus("hh");
		mockMvc.perform(put("/api/purchaseOrder").contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(updatedPurchaseOrder))).andExpect(status().isOk());

		// Validate the purchaseOrder in the database
		List<PurchaseOrder> purchaseOrderList = purchaseOrderRepository.findAll();
		
		assertThat(purchaseOrderList).hasSize(databaseSizeBeforeUpdate);
		PurchaseOrder testpurchaseOrder = purchaseOrderList.get(purchaseOrderList.size() - 1);
		
		assertThat(testpurchaseOrder.getTermsAndConditions()).isEqualTo("cc");
		assertThat(testpurchaseOrder.getNotes()).isEqualTo("dd");
		assertThat(testpurchaseOrder.getStatus()).isEqualTo("hh");
	}
	
	


	@Test
	@Transactional
	@Order(3)
	public void deletepurchaseOrder() throws Exception {

//	    	Id Generated Automatically using MockMvc

		purchaseOrder.setStatus("aa");
	

		purchaseOrderRepository.saveAndFlush(purchaseOrder);

		// Deactivate the purchaseOrder

		mockMvc.perform(delete("/api/purchaseOrder/{id}", purchaseOrder.getId()).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		// Validate the database contains one less item
		Optional<PurchaseOrder> purchaseOrder1 = purchaseOrderRepository.findById(purchaseOrder.getId());
		assertThat(purchaseOrder1.get().getStatus()).isEqualTo("DEACTIVE");

	}
	
	
//	@Test
//	@Transactional
//	@Order(3)
//	public void testSearchpurchaseOrderController() throws IOException, Exception {
//
////	    	Id Generated Automatically using MockMvc
//
//		purchaseOrder.setFirstName("a");
//		purchaseOrder.setMiddleName("b");
//		purchaseOrder.setLastName("c");
//		purchaseOrder.phoneNumber("d");
//		purchaseOrder.setEmail("e");
//		purchaseOrder.setAddress("f");
//		purchaseOrder.setZipCode("g");
//		purchaseOrder.setStatus("h");
//
//		purchaseOrderRepository.saveAndFlush(purchaseOrder);
//
//		// Initialize the database
//
//		int databaseSizeBeforeUpdate = purchaseOrderRepository.findAll().size();
//
//		// Search the purchaseOrder
//
//	
//	}
//	
	

}
