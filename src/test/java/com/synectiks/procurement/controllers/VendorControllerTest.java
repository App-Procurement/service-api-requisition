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
import com.synectiks.procurement.business.service.VendorService;
import com.synectiks.procurement.domain.Vendor;
import com.synectiks.procurement.repository.VendorRepository;
import com.synectiks.procurement.web.rest.TestUtil;

@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
@SpringBootTest
@ActiveProfiles("Vendor controller test")
@AutoConfigureMockMvc
@WithMockUser
class VendorControllerTest {

	@Autowired
	private VendorRepository vendorRepository;

	@Autowired
	private MockMvc mockMvc;
	
	@Mock
	VendorService vendorService;
	

	Vendor vendor = new Vendor();
	
	private ObjectMapper mapper = new ObjectMapper();

	@Test
	@Transactional
	@Order(1)
	public void testAddVendorController() throws IOException, Exception {

		vendor.setFirstName("a");
		vendor.setMiddleName("b");
		vendor.setLastName("c");
		vendor.setPhoneNumber("d");
		vendor.setEmail("e");
		vendor.setAddress("f");
		vendor.setZipCode("g");
		vendor.setStatus("h");

		int databaseSizeBeforeCreate = vendorRepository.findAll().size();

	
		mockMvc.perform(post("/api/vendor").contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(vendor)));

		List<Vendor> vendor1 = vendorRepository.findAll();
		assertThat(vendor1).hasSize(databaseSizeBeforeCreate + 1);
		Vendor testVendor = vendor1.get(vendor1.size() - 1);

		assertThat(testVendor.getFirstName()).isEqualTo("a");
		assertThat(testVendor.getMiddleName()).isEqualTo("b");
		assertThat(testVendor.getLastName()).isEqualTo("c");
		assertThat(testVendor.getPhoneNumber()).isEqualTo("d");
		assertThat(testVendor.getEmail()).isEqualTo("e");
		assertThat(testVendor.getAddress()).isEqualTo("f");
		assertThat(testVendor.getZipCode()).isEqualTo("g");
		assertThat(testVendor.getStatus()).isEqualTo("h");
	}

	@Test
	@Transactional
	@Order(2)
	public void testUpdateVendorController() throws IOException, Exception {

//	    	Id Generated Automatically using MockMvc

		vendor.setFirstName("a");
		vendor.setMiddleName("b");
		vendor.setLastName("c");
		vendor.setPhoneNumber("d");
		vendor.setEmail("e");
		vendor.setAddress("f");
		vendor.setZipCode("g");
		vendor.setStatus("h");

		vendorRepository.saveAndFlush(vendor);

		// Initialize the database

		int databaseSizeBeforeUpdate = vendorRepository.findAll().size();

		// Update the vendor

		Vendor updatedVendor = vendorRepository.findById(vendor.getId()).get();
		// Disconnect from session so that the updates on updatedVendor are not directly
		// saved in db

		updatedVendor.setFirstName("mohit");
		updatedVendor.setMiddleName("Kumar");
		updatedVendor.setLastName("Sharma");
		updatedVendor.setPhoneNumber("1234567890");
		updatedVendor.setEmail("mohitksharma@gmail.com");
		updatedVendor.setAddress("m.s.b ka rasta jhori bazar,jaipur");
		updatedVendor.setZipCode("302003");
		updatedVendor.setStatus("Active");

		mockMvc.perform(put("/api/vendor").contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(updatedVendor))).andExpect(status().isOk());

		// Validate the Vendor in the database
		List<Vendor> vendorList = vendorRepository.findAll();
		assertThat(vendorList).hasSize(databaseSizeBeforeUpdate);
		Vendor testVendor = vendorList.get(vendorList.size() - 1);
		assertThat(testVendor.getFirstName()).isEqualTo("mohit");
		assertThat(testVendor.getMiddleName()).isEqualTo("Kumar");
		assertThat(testVendor.getLastName()).isEqualTo("Sharma");
		assertThat(testVendor.getPhoneNumber()).isEqualTo("1234567890");
		mailto:assertThat(testVendor.getEmail()).isEqualTo("mohitksharma@gmail.com");
		assertThat(testVendor.getAddress()).isEqualTo("m.s.b ka rasta jhori bazar,jaipur");
		assertThat(testVendor.getZipCode()).isEqualTo("302003");
		assertThat(testVendor.getStatus()).isEqualTo("Active");
	}
	
	


	@Test
	@Transactional
	@Order(3)
	public void deleteVendor() throws Exception {

//	    	Id Generated Automatically using MockMvc

		vendor.setFirstName("mohit");
		vendor.setMiddleName("Kumar");
		vendor.setLastName("Sharma");
		vendor.setPhoneNumber("1234567890");
		mailto:vendor.setEmail("mohitksharma@gmail.com");
		vendor.setAddress("m.s.b ka rasta jhori bazar,jaipur");
		vendor.setZipCode("302003");
		vendor.setStatus("Active");

		vendorRepository.saveAndFlush(vendor);

		// Deactivate the vendor

		mockMvc.perform(delete("/api/vendor/{id}", vendor.getId()).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		// Validate the database contains one less item
		Optional<Vendor> vendor1 = vendorRepository.findById(vendor.getId());
		assertThat(vendor1.get().getStatus()).isEqualTo("DEACTIVE");

	}
	
	
//	@Test
//	@Transactional
//	@Order(3)
//	public void testSearchVendorController() throws IOException, Exception {
//
////	    	Id Generated Automatically using MockMvc
//
//		vendor.setFirstName("a");
//		vendor.setMiddleName("b");
//		vendor.setLastName("c");
//		vendor.phoneNumber("d");
//		vendor.setEmail("e");
//		vendor.setAddress("f");
//		vendor.setZipCode("g");
//		vendor.setStatus("h");
//
//		vendorRepository.saveAndFlush(vendor);
//
//		// Initialize the database
//
//		int databaseSizeBeforeUpdate = vendorRepository.findAll().size();
//
//		// Search the vendor
//
//	
//	}
//	
	

}
