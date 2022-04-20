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
import com.synectiks.procurement.business.service.CommitteeService;
import com.synectiks.procurement.domain.Committee;
import com.synectiks.procurement.repository.CommitteeRepository;
import com.synectiks.procurement.web.rest.TestUtil;

@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
@SpringBootTest
@ActiveProfiles("Committee controller test")
@AutoConfigureMockMvc
@WithMockUser
class CommitteeControllerTest {

	@Autowired
	private CommitteeRepository committeeRepository;

	@Autowired
	private MockMvc mockMvc;
	
	@Mock
	CommitteeService committeeService;
	

	Committee committee = new Committee();
	
	private ObjectMapper mapper = new ObjectMapper();

	@Test
	@Transactional
	@Order(1)
	public void testAddCommitteeController() throws IOException, Exception {

		committee.setName("a");
		committee.setType("b");
		committee.setNotes("c");
		committee.setStatus("h");

		int databaseSizeBeforeCreate = committeeRepository.findAll().size();

	
		mockMvc.perform(post("/api/committee").contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(committee)));

		List<Committee> committee1 = committeeRepository.findAll();
		assertThat(committee1).hasSize(databaseSizeBeforeCreate + 1);
		Committee testCommittee = committee1.get(committee1.size() - 1);

		assertThat(testCommittee.getName()).isEqualTo("a");
		assertThat(testCommittee.getType()).isEqualTo("b");
		assertThat(testCommittee.getNotes()).isEqualTo("c");
		assertThat(testCommittee.getStatus()).isEqualTo("h");
	}

	@Test
	@Transactional
	@Order(2)
	public void testUpdateCommitteeController() throws IOException, Exception {

//	    	Id Generated Automatically using MockMvc

		committee.setName("a");
		committee.setType("b");
		committee.setNotes("c");
		committee.setStatus("h");

		committeeRepository.saveAndFlush(committee);

		// Initialize the database

		int databaseSizeBeforeUpdate = committeeRepository.findAll().size();

		// Update the vendor

		Committee updatedCommittee = committeeRepository.findById(committee.getId()).get();
		// Disconnect from session so that the updates on updatedVendor are not directly
		// saved in db

		updatedCommittee.setName("mohit");
		updatedCommittee.setType("Kumar");
		updatedCommittee.setNotes("Sharma");
		updatedCommittee.setStatus("Active");

		mockMvc.perform(put("/api/committee").contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(updatedCommittee))).andExpect(status().isOk());

		// Validate the Vendor in the database
		List<Committee> committeeList = committeeRepository.findAll();
		assertThat(committeeList).hasSize(databaseSizeBeforeUpdate);
		Committee testCommittee = committeeList.get(committeeList.size() - 1);
		assertThat(testCommittee.getName()).isEqualTo("mohit");
		assertThat(testCommittee.getType()).isEqualTo("Kumar");
		assertThat(testCommittee.getNotes()).isEqualTo("Sharma");
		assertThat(testCommittee.getStatus()).isEqualTo("Active");
	}
	
	


	@Test
	@Transactional
	@Order(3)
	public void testDeleteCommittee() throws Exception {

//	    	Id Generated Automatically using MockMvc

		committee.setName("mohit");
		committee.setType("Kumar");
		committee.setNotes("Sharma");
		committee.setStatus("Active");

		committeeRepository.saveAndFlush(committee);

		// Deactivate the vendor

		mockMvc.perform(delete("/api/committee/{id}", committee.getId()).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		// Validate the database contains one less item
		Optional<Committee> committee1 = committeeRepository.findById(committee.getId());
		assertThat(committee1.get().getStatus()).isEqualTo("DEACTIVE");

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
