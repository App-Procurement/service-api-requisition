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
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.synectiks.procurement.domain.Rules;
import com.synectiks.procurement.repository.RequisitionRepository;
import com.synectiks.procurement.repository.RulesRepository;
import com.synectiks.procurement.web.rest.errors.DataNotFoundException;
import com.synectiks.procurement.web.rest.errors.IdNotFoundException;
import com.synectiks.procurement.web.rest.errors.NegativeIdException;
import com.synectiks.procurement.web.rest.errors.UniqueConstraintException;

@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
@SpringBootTest
@ActiveProfiles("Rules test")
class RulesServiceTest {

	@Autowired
	RulesService rulesServiceAuto;
	
	@InjectMocks
	RulesService rulesService;
	
	Rules rules = new Rules();
	
	private ObjectMapper mapper = new ObjectMapper();
	
	@Mock
	RulesRepository rulesRepository;
	
	@Mock
	private RequisitionRepository requisitionRepository;
	
	@Test
	void testAddRules() {
		
		ObjectNode obj = mapper.createObjectNode();
		obj.put("name", "aaaaaaaaaa");
		obj.put("description","aaaa");
		obj.put("rule","pppp");
		
		try {
			rules = rulesServiceAuto.addRules(obj);
		} catch (UniqueConstraintException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertThat(rules.getName()).isEqualTo("aaaaaaaaaa");
		assertThat(rules.getDescription()).isEqualTo("aaaa");
		assertThat(rules.getRule()).isEqualTo("pppp");
	}
	@Test
	public void searchRulesTest() throws NegativeIdException {

		rules.setName("aaaaaaaaaa");
		rules.setDescription("aaaa");
		rules.rule("pppp");
		List<Rules> list1 = new ArrayList<>();
		list1.add(rules);

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("id", null);

		Mockito.when(rulesRepository.findAll(Sort.by(Direction.DESC, "id"))).thenReturn(list1);

		List<Rules> list = rulesService.searchRules(map);

		assertThat(list).hasSize(1);
		
		for(Rules e : list) {
			assertThat(e.getName()).isEqualTo("aaaaaaaaaa");
			assertThat(e.getDescription()).isEqualTo("aaaa");
		}
	}
	@Test
	public void updateRulesTest() throws UniqueConstraintException, JSONException, IdNotFoundException, NegativeIdException, DataNotFoundException{

		ObjectNode obj = mapper.createObjectNode();

		obj.put("ruleId", "1");
		obj.put("name", "aaaaaaaaaa");
		obj.put("description","aaaa");
		obj.put("rule","pppp");

		rules.setName("aaaaaaaaaa");
		rules.setDescription("aaaa");
		rules.rule("pppp");
		
		Rules rules3 = new Rules();
		rules3.setId(1L);
		rules3.setName("qq");
		rules3.setDescription("qqqq");
		rules3.rule("qqqqqq");
		
		
		

		Optional<Rules> rules2 = Optional.of(rules);

		Mockito.when(rulesRepository.findById(1L)).thenReturn(rules2);

		Mockito.when(rulesRepository.save(rules)).thenReturn(rules3);

		Rules rules4;
	
		rules4 = rulesService.updateRules(obj);
		assertThat(rules4.getName()).isEqualTo("qq");
		assertThat(rules4.getDescription()).isEqualTo("qqqq");
		assertThat(rules4.getRule()).isEqualTo("qqqqqq");

	}

}
