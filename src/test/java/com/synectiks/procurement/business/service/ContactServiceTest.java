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
import com.synectiks.procurement.domain.Contact;
import com.synectiks.procurement.repository.ContactActivityRepository;
import com.synectiks.procurement.repository.ContactRepository;
import com.synectiks.procurement.web.rest.errors.DataNotFoundException;
import com.synectiks.procurement.web.rest.errors.IdNotFoundException;
import com.synectiks.procurement.web.rest.errors.NegativeIdException;

@ActiveProfiles("Contact service test")
@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class ContactServiceTest {

	@InjectMocks
	ContactService contactService;

	@Autowired
	ContactService contactServiceAuto;

	@Mock
	ContactRepository contactRepository;
	
	@Mock
	ContactActivityRepository car;

	private ObjectMapper mapper = new ObjectMapper();

	Contact contact = new Contact();

	@Test
	public void addcontactTest() {

		ObjectNode obj = mapper.createObjectNode();

		obj.put("firstName", "a");
		obj.put("middleName", "b");
		obj.put("lastName", "c");
		obj.put("phoneNumber", "d");
		obj.put("email", "e");
		obj.put("status", "h");

		contact = contactServiceAuto.addContact(obj);

		assertThat(contact.getFirstName()).isEqualTo("a");
		assertThat(contact.getMiddleName()).isEqualTo("b");
		assertThat(contact.getLastName()).isEqualTo("c");
		assertThat(contact.getPhoneNumber()).isEqualTo("d");
		assertThat(contact.getEmail()).isEqualTo("e");
		assertThat(contact.getStatus()).isEqualTo("h");

	}

	@Test
	public void searchcontactTest() throws NegativeIdException {

		contact.setFirstName("a");
		contact.setEmail("b");

		List<Contact> list1 = new ArrayList<>();
		list1.add(contact);

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("id", null);

		Mockito.when(contactRepository.findAll(Sort.by(Direction.DESC, "id"))).thenReturn(list1);

		List<Contact> list = contactService.searchContact(map);

		assertThat(list).hasSize(1);
	}

	@Test
	public void updatecontactTest() {

		ObjectNode obj = mapper.createObjectNode();

		obj.put("id", "1");
		obj.put("firstName", "aa");
		obj.put("lastName", "bb");

		contact.setId(1L);
		contact.setFirstName("a");
		contact.setLastName("b");


		Contact contact3 = new Contact();
		contact3.setId(1L);
		contact3.setFirstName("aa");
		contact3.setLastName("bb");

		Optional<Contact> contact2 = Optional.of(contact);
		
		Mockito.when(contactRepository.findById(1L)).thenReturn(contact2);
		
		Mockito.when(contactRepository.save(contact)).thenReturn(contact3);
		
		
		Contact contact4;
		try {
			contact4 = contactService.updateContact(obj);
			assertThat(contact4.getFirstName()).isEqualTo("aa");
			assertThat(contact4.getLastName()).isEqualTo("bb");
		} catch (NegativeIdException | IdNotFoundException | DataNotFoundException e) {
			e.printStackTrace();
		}

	}
}
