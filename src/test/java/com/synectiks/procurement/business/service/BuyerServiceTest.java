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
import com.synectiks.procurement.domain.Buyer;
import com.synectiks.procurement.repository.BuyerRepository;
import com.synectiks.procurement.web.rest.errors.DataNotFoundException;
import com.synectiks.procurement.web.rest.errors.IdNotFoundException;
import com.synectiks.procurement.web.rest.errors.NegativeIdException;

@ActiveProfiles("Buyer service test")
@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class BuyerServiceTest {

	@InjectMocks
	BuyerService buyerService;

	@Autowired
	BuyerService buyerServiceAuto; 

	@Mock
	BuyerRepository buyerRepository;

	private ObjectMapper mapper = new ObjectMapper();

	Buyer buyer = new Buyer();

	@Test
	public void addBuyerTest() {

		ObjectNode obj = mapper.createObjectNode();

		obj.put("firstName", "a");
		obj.put("middleName", "b");
		obj.put("lastName", "c");
		obj.put("phoneNumber", "d");
		obj.put("email", "e");
		obj.put("address", "f");
		obj.put("zipCode", "g");
		obj.put("status", "h");

		buyer = buyerServiceAuto.addBuyer(obj);

		assertThat(buyer.getFirstName()).isEqualTo("a");
		assertThat(buyer.getMiddleName()).isEqualTo("b");
		assertThat(buyer.getLastName()).isEqualTo("c");
		assertThat(buyer.getPhoneNumber()).isEqualTo("d");
		assertThat(buyer.getEmail()).isEqualTo("e");
		assertThat(buyer.getAddress()).isEqualTo("f");
		assertThat(buyer.getZipCode()).isEqualTo("g");
		assertThat(buyer.getStatus()).isEqualTo("h");

	}

	@Test
	public void searchBuyerTest() throws NegativeIdException {

		buyer.setAddress("a");
		buyer.setEmail("b");

		List<Buyer> list1 = new ArrayList<>();
		list1.add(buyer);

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("id", null);

		Mockito.when(buyerRepository.findAll(Sort.by(Direction.DESC, "id"))).thenReturn(list1);

		List<Buyer> list = buyerService.searchBuyer(map);

		assertThat(list).hasSize(1);
	}

	@Test
	public void updateBuyerTest() {

		ObjectNode obj = mapper.createObjectNode();

		obj.put("id", "1");
		obj.put("firstName", "aa");
		obj.put("lastName", "bb");
		
		buyer.setId(1L);
		buyer.setFirstName("a");
		buyer.setLastName("b");

		Buyer buyer3 = new Buyer();
		buyer3.setId(1L);
		buyer3.setFirstName("aa");
		buyer3.setLastName("bb");


		Optional<Buyer> buyer2 = Optional.of(buyer);

		Mockito.when(buyerRepository.findById(1L)).thenReturn(buyer2);

		Mockito.when(buyerRepository.save(buyer)).thenReturn(buyer3);

		Buyer buyer4;
		try {
			buyer4 = buyerService.updateBuyer(obj);
			assertThat(buyer4.getFirstName()).isEqualTo("aa");
			assertThat(buyer4.getLastName()).isEqualTo("bb");
		} catch (NegativeIdException | IdNotFoundException | DataNotFoundException e) {
			e.printStackTrace();
		}

	}
}
