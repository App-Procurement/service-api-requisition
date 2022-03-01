package com.synectiks.procurement.controllers;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.synectiks.procurement.business.service.DepartmentService;
import com.synectiks.procurement.domain.Department;

import io.github.jhipster.web.util.HeaderUtil;

@RestController
@RequestMapping("/api")
//@Api(value = "/api", tags = "Department Management")
public class DepartmentController {
	private static final Logger logger = LoggerFactory.getLogger(DepartmentController.class);

	@Autowired
	private DepartmentService departmentService;

	@PostMapping("/department")
	public ResponseEntity<Department> addDepartment(@RequestBody ObjectNode obj) {
		logger.info("Request to add department");
		Department department = departmentService.addDepartment(obj);
		return ResponseEntity.status(HttpStatus.OK).body(department);
	}

	@PutMapping("/department")
	public ResponseEntity<Department> updateDepartment(@RequestBody ObjectNode obj) {
		logger.info("Request to update Department");
		Department department = departmentService.updateDepartment(obj);
		return ResponseEntity.status(HttpStatus.OK).body(department);

	}

	@GetMapping("/department")
	public ResponseEntity<List<Department>> searchDepartment(@RequestParam Map<String, String> requestObj) {
		logger.info("Request to get department on given filter criteria");
		List<Department> list = departmentService.searchDepartment(requestObj);
		return ResponseEntity.status(HttpStatus.OK).body(list);
	}

	@DeleteMapping("/department/{id}")
	public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
		departmentService.deleteDepartment(id);
		return ResponseEntity.noContent()
				.headers(HeaderUtil.createEntityDeletionAlert("department", false, "department", id.toString()))
				.build();
	}

//	@GetMapping("/getAllDepartment")
//	private ResponseEntity<Status> getAllDepartment() {
//		logger.info("Request to get all Department");
//		Status st = new Status();
//		try {
//			List<Department> list = departmentService.getAllDepartment();
//			if (list == null) {
//				logger.error("Search all department failed");
//				st.setCode(HttpStatus.EXPECTATION_FAILED.value());
//				st.setType("ERROR");
//				st.setMessage("Search all department failed");
//				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
//			}
//			st.setCode(HttpStatus.OK.value());
//			st.setType("SUCCESS");
//			st.setMessage("Search all department successful");
//			st.setObject(list);
//			return ResponseEntity.status(HttpStatus.OK).body(st);
//		} catch (Exception e) {
//			logger.error("Search all department failed. Exception: ", e);
//			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
//			st.setType("ERROR");
//			st.setMessage("Search all department failed");
//			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
//		}
//	}

	@GetMapping("/department/{id}")
	public ResponseEntity<Department> getDepartment(@PathVariable Long id) {
		logger.info("Getting department by id: " + id);
		Department department = departmentService.getDepartment(id);
		return ResponseEntity.status(HttpStatus.OK).body(department);
	}

}
