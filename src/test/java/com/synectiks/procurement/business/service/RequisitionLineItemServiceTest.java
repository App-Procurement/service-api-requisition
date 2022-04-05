package com.synectiks.procurement.business.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
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
import com.synectiks.procurement.domain.Requisition;
import com.synectiks.procurement.domain.RequisitionLineItem;
import com.synectiks.procurement.repository.RequisitionLineItemActivityRepository;
import com.synectiks.procurement.repository.RequisitionLineItemRepository;
import com.synectiks.procurement.repository.RequisitionRepository;
import com.synectiks.procurement.web.rest.errors.DataNotFoundException;
import com.synectiks.procurement.web.rest.errors.IdNotFoundException;
import com.synectiks.procurement.web.rest.errors.NegativeIdException;

@ActiveProfiles("RequisitionLineItem service test")
@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class RequisitionLineItemServiceTest {

	@InjectMocks
	RequisitionLineItemService requisitionLineItemService;

	@Autowired
	RequisitionLineItemService requisitionLineItemServiceAuto;

	@Mock
	RequisitionLineItemRepository requisitionLineItemRepository;

	@Mock
	private RequisitionLineItemActivityService requisitionLineItemActivityService;

	@Mock
	private RequisitionRepository requisitionRepository;

	@Mock
	private RequisitionLineItemActivityRepository requisitionLineItemActivityRepository;

	@Mock
	RequisitionLineItem requisitionLineItemMock;

	private ObjectMapper mapper = new ObjectMapper();

	RequisitionLineItem requisitionLineItem = new RequisitionLineItem();

	Requisition requisition = new Requisition();

	@Test
	public void addrequisitionLineItemTest() {

		ObjectNode obj = mapper.createObjectNode();

		obj.put("itemDescription", "a");
		obj.put("status", "b");
		obj.put("priority", "c");
		obj.put("notes", "d");
		obj.put("requisitionId", "1");

		requisition.setId(1L);
		requisition.setCreatedBy("s");
		requisition.setNotes("t");

		requisitionLineItem.setItemDescription("a");
		requisitionLineItem.setStatus("b");
		requisitionLineItem.setPriority("c");
		requisitionLineItem.setNotes("d");

		Mockito.when(requisitionLineItemRepository.save(requisitionLineItem)).thenReturn(requisitionLineItem);

		requisitionLineItem = requisitionLineItemService.addRequisitionLineItem(requisitionLineItem);

		assertThat(requisitionLineItem.getItemDescription()).isEqualTo("a");
		assertThat(requisitionLineItem.getStatus()).isEqualTo("b");
		assertThat(requisitionLineItem.getPriority()).isEqualTo("c");
		assertThat(requisitionLineItem.getNotes()).isEqualTo("d");

	}

	@Test
	public void searchrequisitionLineItemTest() throws NegativeIdException {

		requisitionLineItem.setItemDescription("a");
		requisitionLineItem.setPriority("b");

		List<RequisitionLineItem> list1 = new ArrayList<>();
		list1.add(requisitionLineItem);

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("id", null);

		Mockito.when(requisitionLineItemRepository.findAll(Sort.by(Direction.DESC, "id"))).thenReturn(list1);

		List<RequisitionLineItem> list = requisitionLineItemService.searchRequisitionLineItem(map);

		assertThat(list).hasSize(1);

		for (RequisitionLineItem e : list) {
			assertThat(e.getItemDescription()).isEqualTo("a");
			assertThat(e.getPriority()).isEqualTo("b");
		}
	}

	@Test
	public void updaterequisitionLineItemTest() {

		ObjectNode obj = mapper.createObjectNode();

		obj.put("id", "1");
		obj.put("itemDescription", "aa");
		obj.put("priority", "bb");

		requisitionLineItem.setId(1L);
		requisitionLineItem.setItemDescription("a");
		requisitionLineItem.setPriority("b");

		RequisitionLineItem requisitionLineItem3 = new RequisitionLineItem();
		requisitionLineItem3.setId(1L);
		requisitionLineItem3.setItemDescription("aa");
		requisitionLineItem3.setPriority("bb");

		Optional<RequisitionLineItem> requisitionLineItem2 = Optional.of(requisitionLineItem);

		Mockito.when(requisitionLineItemRepository.findById(1L)).thenReturn(requisitionLineItem2);

//		Mockito.when(requisitionLineItemRepository.save(requisitionLineItem)).thenReturn(requisitionLineItem3);

		RequisitionLineItem requisitionLineItem4;

		try {
			requisitionLineItem4 = requisitionLineItemService.updateRequisitionLineItem(obj.toString(), null);
			assertThat(requisitionLineItem4).isNull();

		} catch (IOException | IdNotFoundException | NegativeIdException | DataNotFoundException e) {

			e.printStackTrace();
		}

	}
}
