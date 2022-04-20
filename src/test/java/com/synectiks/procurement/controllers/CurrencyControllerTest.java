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
import com.synectiks.procurement.business.service.CurrencyService;
import com.synectiks.procurement.domain.Currency;
import com.synectiks.procurement.repository.CurrencyRepository;
import com.synectiks.procurement.web.rest.TestUtil;

@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
@SpringBootTest
@ActiveProfiles("Currency controller test")
@AutoConfigureMockMvc
@WithMockUser
class CurrencyControllerTest {

	@Autowired
	private CurrencyRepository currencyRepository;

	@Autowired
	private MockMvc mockMvc;
	
	@Mock
	CurrencyService currencyService;
	

	Currency currency = new Currency();
	
	private ObjectMapper mapper = new ObjectMapper();

	@Test
	@Transactional
	@Order(1)
	public void testAddcurrencyController() throws IOException, Exception {

		currency.setCode("a");
		currency.setCountryCode("b");
		currency.setCountryName("c");

		int databaseSizeBeforeCreate = currencyRepository.findAll().size();

	
		mockMvc.perform(post("/api/currency").contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(currency)));

		List<Currency> currency1 = currencyRepository.findAll();
		assertThat(currency1).hasSize(databaseSizeBeforeCreate + 1);
		Currency testcurrency = currency1.get(currency1.size() - 1);

		assertThat(testcurrency.getCode()).isEqualTo("a");
		assertThat(testcurrency.getCountryCode()).isEqualTo("b");
		assertThat(testcurrency.getCountryName()).isEqualTo("c");
	}

	@Test
	@Transactional
	@Order(2)
	public void testUpdatecurrencyController() throws IOException, Exception {

//	    	Id Generated Automatically using MockMvc

		currency.setCode("a");
		currency.setCountryCode("b");
		currency.setCountryName("c");

		currencyRepository.saveAndFlush(currency);

		// Initialize the database

		int databaseSizeBeforeUpdate = currencyRepository.findAll().size();

		// Update the currency

		Currency updatedcurrency = currencyRepository.findById(currency.getId()).get();
		// Disconnect from session so that the updates on updatedcurrency are not directly
		// saved in db

		updatedcurrency.setCode("aa");
		updatedcurrency.setCountryCode("bb");
		updatedcurrency.setCountryName("cc");

		mockMvc.perform(put("/api/currency").contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(updatedcurrency))).andExpect(status().isOk());

		// Validate the currency in the database
		List<Currency> currencyList = currencyRepository.findAll();
		assertThat(currencyList).hasSize(databaseSizeBeforeUpdate);
		Currency testcurrency = currencyList.get(currencyList.size() - 1);
		assertThat(testcurrency.getCode()).isEqualTo("aa");
		assertThat(testcurrency.getCountryCode()).isEqualTo("bb");
		assertThat(testcurrency.getCountryName()).isEqualTo("cc");
	}
	
	


	@Test
	@Transactional
	@Order(3)
	public void deletecurrency() throws Exception {

//	    	Id Generated Automatically using MockMvc

		currency.setStatus("ACTIVE");

		currencyRepository.saveAndFlush(currency);

		// Deactivate the currency

		mockMvc.perform(delete("/api/currency/{id}", currency.getId()).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		// Validate the database contains one less item
		Optional<Currency> currency1 = currencyRepository.findById(currency.getId());
		assertThat(currency1.get().getStatus()).isEqualTo("DEACTIVE");

	}
	
	
//	@Test
//	@Transactional
//	@Order(3)
//	public void testSearchcurrencyController() throws IOException, Exception {
//
////	    	Id Generated Automatically using MockMvc
//
//		currency.setFirstName("a");
//		currency.setMiddleName("b");
//		currency.setLastName("c");
//		currency.phoneNumber("d");
//		currency.setEmail("e");
//		currency.setAddress("f");
//		currency.setZipCode("g");
//		currency.setStatus("h");
//
//		currencyRepository.saveAndFlush(currency);
//
//		// Initialize the database
//
//		int databaseSizeBeforeUpdate = currencyRepository.findAll().size();
//
//		// Search the currency
//
//	
//	}
//	
	

}
