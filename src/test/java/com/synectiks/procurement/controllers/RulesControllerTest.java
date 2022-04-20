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
import com.synectiks.procurement.business.service.RulesService;
import com.synectiks.procurement.domain.Rules;
import com.synectiks.procurement.repository.RulesRepository;
import com.synectiks.procurement.web.rest.TestUtil;

@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
@SpringBootTest
@ActiveProfiles("Rules controller test")
@AutoConfigureMockMvc
@WithMockUser
class RulesControllerTest {

	@Autowired
	private RulesRepository rulesRepository;

	@Autowired
	private MockMvc mockMvc;
	
	@Mock
	RulesService rulesService;

	Rules rules = new Rules();
	
	private ObjectMapper mapper = new ObjectMapper();

	@Test
	@Transactional
	@Order(1)
	public void testAddRulesController() throws IOException, Exception {

		rules.setName("a");
		rules.setDescription("b");
		rules.rule("c");

		int databaseSizeBeforeCreate = rulesRepository.findAll().size();

	
		mockMvc.perform(post("/api/rules").contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(rules)));

		List<Rules> rules1 = rulesRepository.findAll();
		assertThat(rules1).hasSize(databaseSizeBeforeCreate + 1);
		Rules testRules = rules1.get(rules1.size() - 1);

		assertThat(testRules.getName()).isEqualTo("a");
		assertThat(testRules.getDescription()).isEqualTo("b");
		assertThat(testRules.getRule()).isEqualTo("c");
	
	}

	@Test
	@Transactional
	@Order(2)
	public void testUpdateRulesController() throws IOException, Exception {

//	    	Id Generated Automatically using MockMvc


		rules.setName("a");
		rules.setDescription("b");
		rules.rule("c");

		rulesRepository.saveAndFlush(rules);

		// Initialize the database

//		int databaseSizeBeforeUpdate = rulesRepository.findAll().size();

		// Update the rules

//		Rules updatedRules = rulesRepository.findById(rules.getId()).get();
		// Disconnect from session so that the updates on updatedrules are not directly
		// saved in db

		ObjectNode obj = mapper.createObjectNode();

		obj.put("ruleId", rules.getId().toString());
		obj.put("name", "aa");
		obj.put("description","bb");
		obj.put("rule","cc");


		mockMvc.perform(put("/api/rules").contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(obj))).andExpect(status().isOk());

		// Validate the rules in the database
		List<Rules> rulesList = rulesRepository.findAll();
//		assertThat(rulesList).hasSize(databaseSizeBeforeUpdate);
		Rules testRules = rulesList.get(rulesList.size() - 1);
		assertThat(testRules.getName()).isEqualTo("AA");
		assertThat(testRules.getDescription()).isEqualTo("bb");
		assertThat(testRules.getRule()).isEqualTo("cc");

	}
	
	


	@Test
	@Transactional
	@Order(3)
	public void deleteRules() throws Exception {

//	    	Id Generated Automatically using MockMvc

		rules.setName("a");
	
		rulesRepository.saveAndFlush(rules);

		// Deactivate the rules

		mockMvc.perform(delete("/api/rules/{id}", rules.getId()).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		// Validate the database contains one less item
		Optional<Rules> rules1 = rulesRepository.findById(rules.getId());
		assertThat(rules1.get().getStatus()).isEqualTo("DEACTIVE");

	}
	
	
//	@Test
//	@Transactional
//	@Order(3)
//	public void testSearchrulesController() throws IOException, Exception {
//
////	    	Id Generated Automatically using MockMvc
//
//		rules.setFirstName("a");
//		rules.setMiddleName("b");
//		rules.setLastName("c");
//		rules.phoneNumber("d");
//		rules.setEmail("e");
//		rules.setAddress("f");
//		rules.setZipCode("g");
//		rules.setStatus("h");
//
//		rulesRepository.saveAndFlush(rules);
//
//		// Initialize the database
//
//		int databaseSizeBeforeUpdate = rulesRepository.findAll().size();
//
//		// Search the rules
//
//	
//	}
//	
	

}
