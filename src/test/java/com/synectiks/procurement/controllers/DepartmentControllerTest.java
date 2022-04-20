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
import com.synectiks.procurement.business.service.DepartmentService;
import com.synectiks.procurement.domain.Department;
import com.synectiks.procurement.repository.DepartmentRepository;
import com.synectiks.procurement.web.rest.TestUtil;

@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
@SpringBootTest
@ActiveProfiles("Cepartment controller test")
@AutoConfigureMockMvc
@WithMockUser
class DepartmentControllerTest {

	@Autowired
	private DepartmentRepository departmentRepository;

	@Autowired
	private MockMvc mockMvc;
	
	@Mock
	DepartmentService departmentService;
	

	Department department = new Department();
	
	private ObjectMapper mapper = new ObjectMapper();

	@Test
	@Transactional
	@Order(1)
	public void testAddDepartmentController() throws IOException, Exception {

		department.setName("a");
		department.setStatus("b");

		int databaseSizeBeforeCreate = departmentRepository.findAll().size();

	
		mockMvc.perform(post("/api/department").contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(department)));

		List<Department> department1 = departmentRepository.findAll();
		assertThat(department1).hasSize(databaseSizeBeforeCreate + 1);
		Department testdepartment = department1.get(department1.size() - 1);

		assertThat(testdepartment.getName()).isEqualTo("a");
		assertThat(testdepartment.getStatus()).isEqualTo("b");
	}

	@Test
	@Transactional
	@Order(2)
	public void testUpdateDepartmentController() throws IOException, Exception {

//	    	Id Generated Automatically using MockMvc

		department.setName("a");
		department.setStatus("b");
	

		departmentRepository.saveAndFlush(department);

		// Initialize the database

		int databaseSizeBeforeUpdate = departmentRepository.findAll().size();

		// Update the department

		Department updateddepartment = departmentRepository.findById(department.getId()).get();
		// Disconnect from session so that the updates on updateddepartment are not directly
		// saved in db

		updateddepartment.setName("aa");
		updateddepartment.setStatus("bb");

		mockMvc.perform(put("/api/department").contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(updateddepartment))).andExpect(status().isOk());

		// Validate the department in the database
		List<Department> departmentList = departmentRepository.findAll();
		assertThat(departmentList).hasSize(databaseSizeBeforeUpdate);
		Department testdepartment = departmentList.get(departmentList.size() - 1);
		assertThat(testdepartment.getName()).isEqualTo("aa");
		assertThat(testdepartment.getStatus()).isEqualTo("bb");
	}
	
	


	@Test
	@Transactional
	@Order(3)
	public void deletedepartment() throws Exception {

//	    	Id Generated Automatically using MockMvc

		department.setStatus("ACTIVE");
		

		departmentRepository.saveAndFlush(department);

		// Deactivate the department

		mockMvc.perform(delete("/api/department/{id}", department.getId()).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		// Validate the database contains one less item
		Optional<Department> department1 = departmentRepository.findById(department.getId());
		assertThat(department1.get().getStatus()).isEqualTo("DEACTIVE");

	}
	
	
//	@Test
//	@Transactional
//	@Order(3)
//	public void testSearchdepartmentController() throws IOException, Exception {
//
////	    	Id Generated Automatically using MockMvc
//
//		department.setFirstName("a");
//		department.setMiddleName("b");
//		department.setLastName("c");
//		department.phoneNumber("d");
//		department.setEmail("e");
//		department.setAddress("f");
//		department.setZipCode("g");
//		department.setStatus("h");
//
//		departmentRepository.saveAndFlush(department);
//
//		// Initialize the database
//
//		int databaseSizeBeforeUpdate = departmentRepository.findAll().size();
//
//		// Search the department
//
//	
//	}
//	
	

}
