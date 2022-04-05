package com.synectiks.procurement.business.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.synectiks.procurement.domain.Currency;
import com.synectiks.procurement.repository.CurrencyRepository;
import com.synectiks.procurement.web.rest.errors.DataNotFoundException;
import com.synectiks.procurement.web.rest.errors.IdNotFoundException;
import com.synectiks.procurement.web.rest.errors.NegativeIdException;

@ActiveProfiles("Currency service test")
@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
@SpringBootTest
class CurrencyServiceTest {
	
	@Autowired
	CurrencyService currencyServiceAuto;
	
	@InjectMocks
	CurrencyService currencyService;
	
	Currency currency = new Currency();
	
	@Mock
	CurrencyRepository currencyRepository;
	
	private ObjectMapper mapper = new ObjectMapper();

	@Test
	void addCurrencyTest() {
		
		ObjectNode obj = mapper.createObjectNode();
		obj.put("code", "12");
		obj.put("countryName", "ind");
		obj.put("countryCode", "3202002");
		currency = currencyServiceAuto.addCurrency(obj);
		
		assertThat(currency.getCode()).isEqualTo("12");
		assertThat(currency.getCountryName()).isEqualTo("ind");
		assertThat(currency.getCountryCode()).isEqualTo("3202002");
		
	}
	
	@Test
	public void searchCurrencyTest()  {

		currency.setCode("12");
		currency.setCountryName("ind");
		currency.setCountryCode("30303");
		List<Currency> list1 = new ArrayList<>();
		list1.add(currency);

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("id", null);

		Mockito.when(currencyRepository.findAll(Sort.by(Direction.DESC, "id"))).thenReturn(list1);

		List<Currency> list;
		
		try {
			list = currencyService.searchCurrency(map);
			assertThat(list).hasSize(1);
			System.out.println("searchCurrency"+list);
		} catch (NegativeIdException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	
	}
	
	@Test
	public void updateCurrencyTest() {

		ObjectNode obj = mapper.createObjectNode();

		obj.put("id", "1");
		currency.setCode("12");
		currency.setCountryName("ind");
		currency.setCountryCode("30303");

		currency.setCode("aa");
		currency.setCountryCode("bb");
		currency.setCountryName("aa");
		
		Currency currency3 = new Currency();
		currency3.setId(1L);
		currency3.setCode("aa");
		currency3.setCountryCode("bb");
		currency3.setCountryName("aa");
		
		

		Optional<Currency> currency2 = Optional.of(currency);

		Mockito.when(currencyRepository.findById(1L)).thenReturn(currency2);

		Mockito.when(currencyRepository.save(currency)).thenReturn(currency3);

		Currency currency4;
		try {
			currency4 = currencyService.updateCurrency(obj);
			assertThat(currency4.getCode()).isEqualTo("aa");
			assertThat(currency4.getCountryCode()).isEqualTo("bb");
			assertThat(currency4.getCountryName()).isEqualTo("aa");
		} catch (NegativeIdException | IdNotFoundException | DataNotFoundException e) {
			e.printStackTrace();
		}

	}

}
