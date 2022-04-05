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
import com.synectiks.procurement.domain.Vendor;
import com.synectiks.procurement.repository.VendorRepository;
import com.synectiks.procurement.web.rest.errors.DataNotFoundException;
import com.synectiks.procurement.web.rest.errors.IdNotFoundException;
import com.synectiks.procurement.web.rest.errors.NegativeIdException;

@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
@SpringBootTest
@ActiveProfiles("Vendor test")
public class VendorServiceTest {

	@InjectMocks
	VendorService vendorService;

	@Autowired
	VendorService vendorServiceAuto;

	@Mock
	VendorRepository vendorRepository;

	private ObjectMapper mapper = new ObjectMapper();

	Vendor vendor = new Vendor();

	@Test
	public void addVendorTest() {

		ObjectNode obj = mapper.createObjectNode();

		obj.put("firstName", "a");
		obj.put("middleName", "b");
		obj.put("lastName", "c");
		obj.put("phoneNumber", "d");
		obj.put("email", "e");
		obj.put("address", "f");
		obj.put("zipCode", "g");
		obj.put("status", "h");

		vendor = vendorServiceAuto.addVendor(obj);

		assertThat(vendor.getFirstName()).isEqualTo("a");
		assertThat(vendor.getMiddleName()).isEqualTo("b");
		assertThat(vendor.getLastName()).isEqualTo("c");
		assertThat(vendor.getPhoneNumber()).isEqualTo("d");
		assertThat(vendor.getEmail()).isEqualTo("e");
		assertThat(vendor.getAddress()).isEqualTo("f");
		assertThat(vendor.getZipCode()).isEqualTo("g");
		assertThat(vendor.getStatus()).isEqualTo("h");

	}

	@Test
	public void searchVendorTest() throws NegativeIdException {

		vendor.setAddress("a");
		vendor.setEmail("b");

		List<Vendor> list1 = new ArrayList<>();
		list1.add(vendor);

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("id", null);

		Mockito.when(vendorRepository.findAll(Sort.by(Direction.DESC, "id"))).thenReturn(list1);

		List<Vendor> list = vendorService.searchVendor(map);

		assertThat(list).hasSize(1);
		
		for(Vendor e : list) {
			assertThat(e.getAddress()).isEqualTo("a");
			assertThat(e.getEmail()).isEqualTo("b");
		}
	}

	@Test
	public void updateVendorTest() {

		ObjectNode obj = mapper.createObjectNode();

		obj.put("id", "1");
		obj.put("firstName", "aa");
		obj.put("lastName", "bb");

		
		Vendor vendor3 = new Vendor();
		vendor3.setId(1L);
		vendor3.setFirstName("aa");
		vendor3.setLastName("bb");
		
		vendor.setId(1L);
		vendor.setFirstName("a");
		vendor.setLastName("b");

		Optional<Vendor> vendor2 = Optional.of(vendor);

		Mockito.when(vendorRepository.findById(1L)).thenReturn(vendor2);

		Mockito.when(vendorRepository.save(vendor)).thenReturn(vendor3);

		Vendor vendor4;
		try {
			vendor4 = vendorService.updateVendor(obj);
			assertThat(vendor4.getFirstName()).isEqualTo("aa");
			assertThat(vendor4.getLastName()).isEqualTo("bb");
		} catch (NegativeIdException | IdNotFoundException | DataNotFoundException e) {
			e.printStackTrace();
		}

	}
}
