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
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.synectiks.procurement.domain.Committee;
import com.synectiks.procurement.repository.CommitteeActivityRepository;
import com.synectiks.procurement.repository.CommitteeRepository;
import com.synectiks.procurement.web.rest.errors.DataNotFoundException;
import com.synectiks.procurement.web.rest.errors.IdNotFoundException;
import com.synectiks.procurement.web.rest.errors.NegativeIdException;

@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class CommitteeServiceTest {

	@InjectMocks
	CommitteeService committeeService;

	@Autowired
	CommitteeService committeeServiceAuto;

	@Mock
	CommitteeRepository committeeRepository;

	@Mock
	CommitteeActivityRepository committeeActivityRepository;

	private ObjectMapper mapper = new ObjectMapper();

	Committee committee = new Committee();

	@Test
	public void addCommitteeTest() {

		ObjectNode obj = mapper.createObjectNode();

		obj.put("name", "a");
		obj.put("type", "b");
		obj.put("status", "c");
		obj.put("notes", "d");

		committee = committeeServiceAuto.addCommittee(obj);

		assertThat(committee.getName()).isEqualTo("a");
		assertThat(committee.getType()).isEqualTo("b");
		assertThat(committee.getStatus()).isEqualTo("c");
		assertThat(committee.getNotes()).isEqualTo("d");

	}

	@Test
	public void searchCommitteeTest() throws NegativeIdException {

		committee.setName("a");
		committee.setType("b");

		List<Committee> list1 = new ArrayList<>();
		list1.add(committee);

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("id", null);

		Mockito.when(committeeRepository.findAll(Sort.by(Direction.DESC, "id"))).thenReturn(list1);

		List<Committee> list = committeeService.searchCommittee(map);

		assertThat(list).hasSize(1);
	}

	@Test
	public void updatecommitteeTest() {

		ObjectNode obj = mapper.createObjectNode();

		obj.put("id", "1");
		obj.put("name", "aa");
		obj.put("type", "bb");

		Committee committee3 = new Committee();
		committee3.setId(1L);
		committee3.setName("aa");
		committee3.setType("bb");

		committee.setId(1L);
		committee.setName("a");
		committee.setType("b");

		Optional<Committee> committee2 = Optional.of(committee);

		Mockito.when(committeeRepository.findById(1L)).thenReturn(committee2);

		Mockito.when(committeeRepository.save(committee)).thenReturn(committee3);

		Committee committee4;
		try {
			committee4 = committeeService.updateCommittee(obj);
			assertThat(committee4.getName()).isEqualTo("aa");
			assertThat(committee4.getType()).isEqualTo("bb");
		} catch (NegativeIdException | IdNotFoundException | DataNotFoundException e) {
			e.printStackTrace();
		}

	}
}
