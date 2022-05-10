//package com.synectiks.procurement.controllers;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.Optional;
//
//import org.junit.jupiter.api.Order;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.synectiks.procurement.business.service.BuyerService;
//import com.synectiks.procurement.domain.Buyer;
//import com.synectiks.procurement.repository.BuyerRepository;
//import com.synectiks.procurement.web.rest.TestUtil;
//
//@RunWith(SpringRunner.class)
//@ExtendWith(MockitoExtension.class)
//@SpringBootTest
//@ActiveProfiles("Buyer controller test")
//@AutoConfigureMockMvc
//@WithMockUser
//class BuyerControllerTest {
//
//	@Autowired
//	private BuyerRepository buyerRepository;
//
//	@Autowired
//	private MockMvc mockMvc;
//	
//	@Mock
//	BuyerService buyerService;
//	
//
//	Buyer buyer = new Buyer();
//	
//	private ObjectMapper mapper = new ObjectMapper();
//
//	@Test
//	@Transactional
//	@Order(1)
//	public void testAddBuyer() throws IOException, Exception {
//
//		buyer.setFirstName("a");
//		buyer.setMiddleName("b");
//		buyer.setLastName("c");
//		buyer.setPhoneNumber("d");
//		buyer.setEmail("e");
//		buyer.setAddress("f");
//		buyer.setZipCode("g");
//		buyer.setStatus("h");
//
//		int databaseSizeBeforeCreate = buyerRepository.findAll().size();
//
//	
//		mockMvc.perform(post("/api/buyer").contentType(MediaType.APPLICATION_JSON)
//				.content(TestUtil.convertObjectToJsonBytes(buyer)));
//
//		List<Buyer> buuyer1 = buyerRepository.findAll();
//		assertThat(buuyer1).hasSize(databaseSizeBeforeCreate + 1);
//		Buyer testBuyer = buuyer1.get(buuyer1.size() - 1);
//
//		assertThat(testBuyer.getFirstName()).isEqualTo("a");
//		assertThat(testBuyer.getMiddleName()).isEqualTo("b");
//		assertThat(testBuyer.getLastName()).isEqualTo("c");
//		assertThat(testBuyer.getPhoneNumber()).isEqualTo("d");
//		assertThat(testBuyer.getEmail()).isEqualTo("e");
//		assertThat(testBuyer.getAddress()).isEqualTo("f");
//		assertThat(testBuyer.getZipCode()).isEqualTo("g");
//		assertThat(testBuyer.getStatus()).isEqualTo("h");
//	}
//
//	@Test
//	@Transactional
//	@Order(2)
//	public void testUpdateBuyerController() throws IOException, Exception {
//
////	    	Id Generated Automatically using MockMvc
//
//		buyer.setFirstName("a");
//		buyer.setMiddleName("b");
//		buyer.setLastName("c");
//		buyer.setPhoneNumber("d");
//		buyer.setEmail("e");
//		buyer.setAddress("f");
//		buyer.setZipCode("g");
//		buyer.setStatus("h");
//
//		buyerRepository.saveAndFlush(buyer);
//
//		// Initialize the database
//
//		int databaseSizeBeforeUpdate = buyerRepository.findAll().size();
//
//		// Update the vendor
//
//		Buyer updatedBuyer = buyerRepository.findById(buyer.getId()).get();
//		// Disconnect from session so that the updates on updatedVendor are not directly
//		// saved in db
//
//		updatedBuyer.setFirstName("mohit");
//		updatedBuyer.setMiddleName("Kumar");
//		updatedBuyer.setLastName("Sharma");
//		updatedBuyer.setPhoneNumber("1234567890");
//		updatedBuyer.setEmail("mohitksharma@gmail.com");
//		updatedBuyer.setAddress("m.s.b ka rasta jhori bazar,jaipur");
//		updatedBuyer.setZipCode("302003");
//		updatedBuyer.setStatus("Active");
//
//		mockMvc.perform(put("/api/buyer").contentType(MediaType.APPLICATION_JSON)
//				.content(TestUtil.convertObjectToJsonBytes(updatedBuyer))).andExpect(status().isOk());
//
//		// Validate the Vendor in the database
//		List<Buyer> buyerList = buyerRepository.findAll();
//		assertThat(buyerList).hasSize(databaseSizeBeforeUpdate);
//		Buyer testBuyer = buyerList.get(buyerList.size() - 1);
//		assertThat(testBuyer.getFirstName()).isEqualTo("mohit");
//		assertThat(testBuyer.getMiddleName()).isEqualTo("Kumar");
//		assertThat(testBuyer.getLastName()).isEqualTo("Sharma");
//		assertThat(testBuyer.getPhoneNumber()).isEqualTo("1234567890");
//		mailto:assertThat(testBuyer.getEmail()).isEqualTo("mohitksharma@gmail.com");
//		assertThat(testBuyer.getAddress()).isEqualTo("m.s.b ka rasta jhori bazar,jaipur");
//		assertThat(testBuyer.getZipCode()).isEqualTo("302003");
//		assertThat(testBuyer.getStatus()).isEqualTo("Active");
//	}
//	
//	
//
//
//	@Test
//	@Transactional
//	@Order(3)
//	public void deleteVendor() throws Exception {
//
////	    	Id Generated Automatically using MockMvc
//
//		buyer.setFirstName("mohit");
//		buyer.setMiddleName("Kumar");
//		buyer.setLastName("Sharma");
//		buyer.setPhoneNumber("1234567890");
//		mailto:buyer.setEmail("mohitksharma@gmail.com");
//		buyer.setAddress("m.s.b ka rasta jhori bazar,jaipur");
//		buyer.setZipCode("302003");
//		buyer.setStatus("Active");
//
//		buyerRepository.saveAndFlush(buyer);
//
//		// Deactivate the vendor
//
//		mockMvc.perform(delete("/api/buyer/{id}", buyer.getId()).accept(MediaType.APPLICATION_JSON))
//				.andExpect(status().isOk());
//
//		// Validate the database contains one less item
//		Optional<Buyer> buyer1 = buyerRepository.findById(buyer.getId());
//		assertThat(buyer1.get().getStatus()).isEqualTo("DEACTIVE");
//
//	}
//	
//	
////	@Test
////	@Transactional
////	@Order(3)
////	public void testSearchVendorController() throws IOException, Exception {
////
//////	    	Id Generated Automatically using MockMvc
////
////		vendor.setFirstName("a");
////		vendor.setMiddleName("b");
////		vendor.setLastName("c");
////		vendor.phoneNumber("d");
////		vendor.setEmail("e");
////		vendor.setAddress("f");
////		vendor.setZipCode("g");
////		vendor.setStatus("h");
////
////		vendorRepository.saveAndFlush(vendor);
////
////		// Initialize the database
////
////		int databaseSizeBeforeUpdate = vendorRepository.findAll().size();
////
////		// Search the vendor
////
////	
////	}
////	
//	
//
//}
