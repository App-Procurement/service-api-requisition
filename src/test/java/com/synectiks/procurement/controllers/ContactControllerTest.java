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
import com.synectiks.procurement.business.service.ContactService;
import com.synectiks.procurement.domain.Contact;
import com.synectiks.procurement.repository.ContactRepository;
import com.synectiks.procurement.web.rest.TestUtil;

@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
@SpringBootTest
@ActiveProfiles("Contact controller test")
@AutoConfigureMockMvc
@WithMockUser
class ContactControllerTest {

	@Autowired
	private ContactRepository contactRepository;

	@Autowired
	private MockMvc mockMvc;
	
	@Mock
	ContactService contactService;
	

	Contact contact = new Contact();
	
	private ObjectMapper mapper = new ObjectMapper();

	@Test
	@Transactional
	@Order(1)
	public void testAddcontactController() throws IOException, Exception {

		contact.setFirstName("a");
		contact.setMiddleName("b");
		contact.setLastName("c");
		contact.setPhoneNumber("d");
		contact.setEmail("e");
		contact.setStatus("h");

		int databaseSizeBeforeCreate = contactRepository.findAll().size();

	
		mockMvc.perform(post("/api/contact").contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(contact)));

		List<Contact> contact1 = contactRepository.findAll();
		assertThat(contact1).hasSize(databaseSizeBeforeCreate + 1);
		Contact testcontact = contact1.get(contact1.size() - 1);

		assertThat(testcontact.getFirstName()).isEqualTo("a");
		assertThat(testcontact.getMiddleName()).isEqualTo("b");
		assertThat(testcontact.getLastName()).isEqualTo("c");
		assertThat(testcontact.getPhoneNumber()).isEqualTo("d");
		assertThat(testcontact.getEmail()).isEqualTo("e");
		assertThat(testcontact.getStatus()).isEqualTo("h");
	}

	@Test
	@Transactional
	@Order(2)
	public void testUpdatecontactController() throws IOException, Exception {

//	    	Id Generated Automatically using MockMvc

		contact.setFirstName("a");
		contact.setMiddleName("b");
		contact.setLastName("c");
		contact.setPhoneNumber("d");
		contact.setEmail("e");
		contact.setStatus("h");

		contactRepository.saveAndFlush(contact);

		// Initialize the database

		int databaseSizeBeforeUpdate = contactRepository.findAll().size();

		// Update the contact

		Contact updatedcontact = contactRepository.findById(contact.getId()).get();
		// Disconnect from session so that the updates on updatedcontact are not directly
		// saved in db

		updatedcontact.setFirstName("aa");
		updatedcontact.setMiddleName("bb");
		updatedcontact.setLastName("cc");
		updatedcontact.setPhoneNumber("dd");
		updatedcontact.setEmail("ee");
		updatedcontact.setStatus("hh");

		mockMvc.perform(put("/api/contact").contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(updatedcontact))).andExpect(status().isOk());

		// Validate the contact in the database
		List<Contact> contactList = contactRepository.findAll();
		assertThat(contactList).hasSize(databaseSizeBeforeUpdate);
		Contact testcontact = contactList.get(contactList.size() - 1);
		assertThat(testcontact.getFirstName()).isEqualTo("aa");
		assertThat(testcontact.getMiddleName()).isEqualTo("bb");
		assertThat(testcontact.getLastName()).isEqualTo("cc");
		assertThat(testcontact.getPhoneNumber()).isEqualTo("dd");
		assertThat(testcontact.getEmail()).isEqualTo("ee");
		assertThat(testcontact.getStatus()).isEqualTo("hh");
	}
	
	


	@Test
	@Transactional
	@Order(3)
	public void deletecontact() throws Exception {

//	    	Id Generated Automatically using MockMvc

		contact.setStatus("ACTIVE");
		

		contactRepository.saveAndFlush(contact);

		// Deactivate the contact

		mockMvc.perform(delete("/api/contact/{id}", contact.getId()).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		// Validate the database contains one less item
		Optional<Contact> contact1 = contactRepository.findById(contact.getId());
		assertThat(contact1.get().getStatus()).isEqualTo("DEACTIVE");

	}
	
	
//	@Test
//	@Transactional
//	@Order(3)
//	public void testSearchcontactController() throws IOException, Exception {
//
////	    	Id Generated Automatically using MockMvc
//
//		contact.setFirstName("a");
//		contact.setMiddleName("b");
//		contact.setLastName("c");
//		contact.phoneNumber("d");
//		contact.setEmail("e");
//		contact.setAddress("f");
//		contact.setZipCode("g");
//		contact.setStatus("h");
//
//		contactRepository.saveAndFlush(contact);
//
//		// Initialize the database
//
//		int databaseSizeBeforeUpdate = contactRepository.findAll().size();
//
//		// Search the contact
//
//	
//	}
//	
	

}
