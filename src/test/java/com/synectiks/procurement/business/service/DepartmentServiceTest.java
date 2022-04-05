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
import com.synectiks.procurement.domain.Department;
import com.synectiks.procurement.repository.DepartmentRepository;
import com.synectiks.procurement.web.rest.errors.DataNotFoundException;
import com.synectiks.procurement.web.rest.errors.IdNotFoundException;
import com.synectiks.procurement.web.rest.errors.NegativeIdException;

@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
@SpringBootTest
@ActiveProfiles("Department test")
class DepartmentServiceTest {

	@Autowired
	DepartmentService departmentServiceAuto;
	
	@InjectMocks
	DepartmentService departmentService;
	
	Department department = new Department();
	
	private ObjectMapper mapper = new ObjectMapper();
	
	@Mock
	DepartmentRepository departmentRepository;
	
	@Test
	void testAddDepartment() {
		ObjectNode obj = mapper.createObjectNode();
		obj.put("name", "HR");
		obj.put("status","Active");
		
		department = departmentServiceAuto.addDepartment(obj);
		
		assertThat(department.getName()).isEqualTo("HR");
		assertThat(department.getStatus()).isEqualTo("Active");
		
	}
	
	@Test
	public void searchCurrencyTest()  {

		department.setName("HR");
		department.setStatus("Active");
		List<Department> list1 = new ArrayList<>();
		list1.add(department);

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("id", null);
		
		Mockito.when(departmentRepository.findAll(Sort.by(Direction.DESC, "id"))).thenReturn(list1);
		
		List<Department> list;
		
		try {
			list = departmentService.searchDepartment(map);
			assertThat(list).hasSize(1);
		} catch (NegativeIdException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	
	}
	
	@Test
	public void updateCurrencyTest() {

		ObjectNode obj = mapper.createObjectNode();

		obj.put("id", "1");
		department.setName("HR");
		department.setStatus("Active");

		department.setName("hr");
		department.setStatus("draft");
		
		
		Department department3 = new Department();
		department3.setId(1L);
		department3.setName("aa");
		department3.setStatus("bb");
		
		
		

		Optional<Department> department2 = Optional.of(department);

		Mockito.when(departmentRepository.findById(1L)).thenReturn(department2);

		Mockito.when(departmentRepository.save(department)).thenReturn(department3);

		Department department4;
		try {
			department4 = departmentService.updateDepartment(obj);
			assertThat(department4.getName()).isEqualTo("aa");
			assertThat(department4.getStatus()).isEqualTo("bb");

		} catch (NegativeIdException | IdNotFoundException | DataNotFoundException e) {
			e.printStackTrace();
		}

	}

}
