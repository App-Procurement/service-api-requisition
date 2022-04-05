package com.synectiks.procurement.business.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.text.ParseException;
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
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.synectiks.procurement.domain.Requisition;
import com.synectiks.procurement.repository.DocumentRepository;
import com.synectiks.procurement.repository.RequisitionActivityRepository;
import com.synectiks.procurement.repository.RequisitionRepository;
import com.synectiks.procurement.repository.VendorRequisitionBucketRepository;
import com.synectiks.procurement.web.rest.errors.DataNotFoundException;
import com.synectiks.procurement.web.rest.errors.IdNotFoundException;
import com.synectiks.procurement.web.rest.errors.NegativeIdException;

@ActiveProfiles("Requisition service test")
@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class RequisitionServiceTest {

	@InjectMocks
	RequisitionService requisitionService;

	@Autowired
	RequisitionService requisitionServiceAuto;

	@Mock
	RequisitionRepository requisitionRepository;

	@Mock
	private RequisitionActivityRepository requisitionActivityRepository;

	@Mock
	private VendorRequisitionBucketRepository vendorRequisitionBucketRepository;

	@Mock
	DocumentRepository documentRepository;

	@Mock
	DocumentService documentService;

	@Mock
	RequisitionLineItemService requisitionLineItemService;
	
	@Mock
	RequisitionActivityService requisitionActivityService;

	private ObjectMapper mapper = new ObjectMapper();

	Requisition requisition = new Requisition();

	@Test
	public void addrequisitionTest() {

		ObjectNode obj = mapper.createObjectNode();

		obj.put("status", "b");
		obj.put("notes", "e");
		obj.put("departmentId", "1");
		obj.put("currencyId", "1");

		try {
			requisition = requisitionServiceAuto.addRequisition(null, null, obj.toString());
		} catch (IOException | JSONException | NegativeIdException e) {
			e.printStackTrace();
		}

		assertThat(requisition.getNotes()).isEqualTo("e");
		assertThat(requisition.getStatus()).isEqualTo("b");

	}

	@Test
	public void searchrequisitionTest() throws NegativeIdException {

		requisition.setId(1L);
		requisition.setStatus("a");
		requisition.setNotes("b");

		List<Requisition> list1 = new ArrayList<>();
		list1.add(requisition);

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("id", null);

		Mockito.when(requisitionRepository.findAll(Sort.by(Direction.DESC, "id"))).thenReturn(list1);

		List<Requisition> list;
		try {
			list = requisitionService.searchRequisition(map);
			assertThat(list).hasSize(1);

			for (Requisition e : list) {
				assertThat(e.getId()).isEqualTo(1L);
				assertThat(e.getStatus()).isEqualTo("a");
				assertThat(e.getNotes()).isEqualTo("b");
			}
		} catch (NegativeIdException | ParseException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void updaterequisitionTest() {

		ObjectNode obj = mapper.createObjectNode();

		obj.put("id", "1");
		obj.put("progressStage", "aa");
		obj.put("notes", "bb");
		obj.put("status", "cc");

		requisition.setId(1L);
		requisition.setProgressStage("a");
		requisition.setNotes("b");

		Requisition requisition3 = new Requisition();
		requisition3.setId(1L);
		requisition3.setProgressStage("aa");
		requisition3.setNotes("bb");

		Optional<Requisition> requisition2 = Optional.of(requisition);

		Mockito.when(requisitionRepository.findById(1L)).thenReturn(requisition2);

		Mockito.when(requisitionRepository.save(requisition)).thenReturn(requisition3);

		Requisition requisition4;

		try {
			requisition4 = requisitionService.updateRequisition(null, null, obj.toString());
			assertThat(requisition4.getProgressStage()).isEqualTo("aa");
			assertThat(requisition4.getNotes()).isEqualTo("bb");
		} catch (IdNotFoundException | NegativeIdException | DataNotFoundException | IOException | JSONException e) {
			e.printStackTrace();
		}

	}
}
